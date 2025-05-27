package com.example.concurrent.domain.usercoupon.optimisticlock.service;

import com.example.concurrent.common.exception.CustomException;
import com.example.concurrent.domain.coupon.entity.Coupon;
import com.example.concurrent.domain.coupon.repository.CouponRepository;
import com.example.concurrent.domain.user.entity.User;
import com.example.concurrent.domain.user.repository.UserRepository;
import com.example.concurrent.domain.usercoupon.optimisticlock.entity.UserCouponOpt;
import com.example.concurrent.domain.usercoupon.optimisticlock.repository.UserCouponOptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCouponOptService {

    private final UserCouponOptRepository userCouponOptRepository;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;

    public void issueCouponUsingOptimisticLock(Long userId, Long couponId) {
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
        userCouponOptRepository.save(UserCouponOpt.create(coupon, user));
    }


}
