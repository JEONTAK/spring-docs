package com.example.concurrent.domain.coupon.controller;

import com.example.concurrent.domain.coupon.dto.request.CouponCreateRequest;
import com.example.concurrent.domain.coupon.dto.response.CouponCreateResponse;
import com.example.concurrent.domain.coupon.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/coupons")
@RequiredArgsConstructor
@Tag(name = "쿠폰 API", description = "쿠폰 API")
public class CouponController {

    private final CouponService couponService;

    //쿠폰 등록
    @Operation(summary = "쿠폰 삽입", description = "요청한 데이터(쿠폰 이름, 쿠폰 가격, 쿠폰 수량)로 쿠폰을 INSERT 합니다.")
    @PostMapping("/create")
    public ResponseEntity<CouponCreateResponse> createCoupon(@RequestBody CouponCreateRequest request) {
        CouponCreateResponse response = couponService.createCoupon(request.getCouponName(), request.getTotalAmount(), request.getDiscountPrice());

        return ResponseEntity.ok(response);
    }

}
