package com.example.concurrent.domain.usercoupon.common.service;

import com.example.concurrent.common.exception.CustomException;
import com.example.concurrent.domain.coupon.entity.Coupon;
import com.example.concurrent.domain.coupon.repository.CouponRepository;
import com.example.concurrent.domain.user.entity.User;
import com.example.concurrent.domain.user.repository.UserRepository;
import com.example.concurrent.domain.usercoupon.common.entity.UserCoupon;
import com.example.concurrent.domain.usercoupon.common.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCouponService {

    private final UserCouponRepository userCouponRepository;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final StringRedisTemplate redisTemplate;
    private final RedissonClient redissonClient;
    private final DataSource dataSource;
    private final ReentrantLock lock = new ReentrantLock();

    public void issueCouponUsingReentrantLock(Long userId, Long couponId) {
        lock.lock();
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."));
            Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "해당 쿠폰을 찾을 수 없습니다."));

            //중복 발급 체크 (부하 테스트시 사용 X)
            /*if (userCouponRepository.existsByUserIdAndCouponId(userId, couponId)) {
                throw new CustomException(HttpStatus.BAD_REQUEST, "이미 발급 받은 쿠폰입니다.");
            }*/

            //쿠폰 소진 체크
            if (coupon.getIssuedAmount() >= coupon.getTotalAmount()) {
                throw new CustomException(HttpStatus.BAD_REQUEST, "해당 쿠폰이 모두 소진되었습니다.");
            }

            //쿠폰 발급
            couponRepository.incrementIssuedAmount(couponId);
            userCouponRepository.save(UserCoupon.create(coupon, user));
        } finally {
            lock.unlock();
        }
    }

    public void issueCouponUsingNamedLock(Long userId, Long couponId) {
        String lockName = "coupon:lock:" + couponId;
        int timeout = 10000;
        try {
            couponRepository.getLock(lockName, timeout);
            User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."));
            Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "해당 쿠폰을 찾을 수 없습니다."));

            //중복 발급 체크 (부하 테스트시 사용 X)
                /*if (userCouponRepository.existsByUserIdAndCouponId(userId, couponId)) {
                    throw new CustomException(HttpStatus.BAD_REQUEST, "이미 발급 받은 쿠폰입니다.");
                }*/

            //쿠폰 소진 체크
            if (coupon.getIssuedAmount() >= coupon.getTotalAmount()) {
                throw new CustomException(HttpStatus.BAD_REQUEST, "해당 쿠폰이 모두 소진되었습니다.");
            }

            //쿠폰 발급
            couponRepository.incrementIssuedAmount(couponId);
            userCouponRepository.save(UserCoupon.create(coupon, user));
        } finally {
            couponRepository.releaseLock(lockName);
        }
    }

    public void issueCouponUsingLettuce(Long userId, Long couponId) {
        String lockKey = "coupon:lock:" + couponId;
        String lockValue = UUID.randomUUID().toString();
        int maxRetries = 40;
        int retryDelayMs = 100;

        try {
            for (int attempt = 0; attempt < maxRetries; attempt++) {
                Boolean locked = redisTemplate.opsForValue()
                        .setIfAbsent(lockKey, lockValue, 10, TimeUnit.SECONDS);

                if (locked != null && locked) {
                    try {
                        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."));
                        Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "해당 쿠폰을 찾을 수 없습니다."));

                        if (coupon.getIssuedAmount() >= coupon.getTotalAmount()) {
                            throw new CustomException(HttpStatus.BAD_REQUEST, "해당 쿠폰이 모두 소진되었습니다.");
                        }

                        couponRepository.incrementIssuedAmount(couponId);
                        userCouponRepository.save(UserCoupon.create(coupon, user));
                        return;
                    } finally {
                        String currentValue = redisTemplate.opsForValue().get(lockKey);
                        if (lockValue.equals(currentValue)) {
                            redisTemplate.delete(lockKey);
                        }
                    }
                } else if (attempt < maxRetries - 1) {
                    Thread.sleep(retryDelayMs);
                }
            }
            throw new CustomException(HttpStatus.TOO_MANY_REQUESTS, "락 획득 실패");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Lettuce 락 처리 중 오류");
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Lettuce 락 처리 중 오류: " + e.getMessage());
        }
    }

    public void issueCouponUsingRedisson(Long userId, Long couponId) {
        RLock lock = redissonClient.getLock("coupon:lock:" + couponId);
        try {
            if (lock.tryLock(10, 30, TimeUnit.SECONDS)) {
                try {
                    User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."));
                    Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "해당 쿠폰을 찾을 수 없습니다."));

                    //중복 발급 체크 (부하 테스트시 사용 X)
                    /*if (userCouponRepository.existsByUserIdAndCouponId(userId, couponId)) {
                        throw new CustomException(HttpStatus.BAD_REQUEST, "이미 발급 받은 쿠폰입니다.");
                    }*/

                    //쿠폰 소진 체크
                    if (coupon.getIssuedAmount() >= coupon.getTotalAmount()) {
                        throw new CustomException(HttpStatus.BAD_REQUEST, "해당 쿠폰이 모두 소진되었습니다.");
                    }

                    //쿠폰 발급
                    couponRepository.incrementIssuedAmount(couponId);
                    userCouponRepository.save(UserCoupon.create(coupon, user));
                } finally {
                    lock.unlock();
                }
            } else {
                throw new CustomException(HttpStatus.TOO_MANY_REQUESTS, "락 획득 실패");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Redisson 락 처리 중 오류");
        }
    }

    public void issueCouponUsingRedisDECR(Long userId, Long couponId) {
        String counterKey = "coupon:remaining:" + couponId;
        try {
            // 사용자 조회
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
            Coupon coupon = couponRepository.findById(couponId)
                    .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "쿠폰을 찾을 수 없습니다."));

            // 중복 발급 체크
            /*if (userCouponRepository.existsByUserIdAndCouponId(userId, couponId)) {
                throw new CustomException(HttpStatus.BAD_REQUEST, "이미 발급받은 쿠폰입니다.");
            }*/

            // DECR로 남은 쿠폰 감소
            Long remaining = redisTemplate.opsForValue().decrement(counterKey);
            if (remaining == null || remaining < 0) {
                redisTemplate.opsForValue().increment(counterKey); // 롤백
                throw new CustomException(HttpStatus.BAD_REQUEST, "쿠폰이 모두 소진되었습니다.");
            }

            // 쿠폰 발급
            couponRepository.incrementIssuedAmount(couponId);
            userCouponRepository.save(UserCoupon.create(coupon, user));
        } catch (Exception e) {
            // 롤백
            redisTemplate.opsForValue().increment(counterKey);
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Redis DECR 처리 중 오류");
        }
    }

    public void initializeRedisCounter(Long couponId, Integer totalAmount) {
        String counterKey = "coupon:remaining:" + couponId;
        redisTemplate.opsForValue().set(counterKey, String.valueOf(totalAmount));
    }
}
