package com.example.concurrent.domain.coupon.service;

import com.example.concurrent.common.exception.CustomException;
import com.example.concurrent.domain.coupon.dto.response.CouponCreateResponse;
import com.example.concurrent.domain.usercoupon.service.UserCouponService;
import com.example.concurrent.domain.coupon.entity.Coupon;
import com.example.concurrent.domain.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponService {

    private final CouponRepository couponRepository;
    private final UserCouponService userCouponService;

    public CouponCreateResponse createCoupon(String couponName, Integer totalAmount, Long discountPrice) {
        if(couponRepository.existsByCouponName(couponName)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "이미 존재하는 쿠폰입니다.");
        }

        Coupon coupon = Coupon.create(couponName, discountPrice, totalAmount, 0);
        couponRepository.save(coupon);
        userCouponService.initializeRedisCounter(coupon.getId(), totalAmount);
        return CouponCreateResponse.of(couponName + "으로 " + discountPrice + "원의 " + totalAmount + "개가 생성 되었습니다.");
    }
}
