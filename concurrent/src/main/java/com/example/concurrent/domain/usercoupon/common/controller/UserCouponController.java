package com.example.concurrent.domain.usercoupon.common.controller;

import com.example.concurrent.domain.usercoupon.common.service.UserCouponService;
import com.example.concurrent.domain.usercoupon.dto.request.UserCouponIssueRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/issueCoupons")
@RequiredArgsConstructor
@Tag(name = "쿠폰발급 API", description = "쿠폰발급 API")
public class UserCouponController {

    private final UserCouponService userCouponService;

    //쿠폰 발급 (Java ReentrantLock)
    @PostMapping("/reentrant-lock")
    public ResponseEntity<Void> usingReentrantLock(@RequestBody UserCouponIssueRequest request) {
        userCouponService.issueCouponUsingReentrantLock(request.getUserId(), request.getCouponId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //쿠폰 발급 (MySQL NamedLock)
    @PostMapping("/named-lock")
    public ResponseEntity<Void> usingNamedLock(@RequestBody UserCouponIssueRequest request) {
        userCouponService.issueCouponUsingNamedLock(request.getUserId(), request.getCouponId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //쿠폰 발급 (Lettuce)
    @PostMapping("/lettuce")
    public ResponseEntity<Void> usingLettuce(@RequestBody UserCouponIssueRequest request) {
        userCouponService.issueCouponUsingLettuce(request.getUserId(), request.getCouponId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //쿠폰 발급 (Redisson)
    @PostMapping("/redisson")
    public ResponseEntity<Void> usingRedisson(@RequestBody UserCouponIssueRequest request) {
        userCouponService.issueCouponUsingRedisson(request.getUserId(), request.getCouponId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //쿠폰 발급 (Redis DECR)
    @PostMapping("/redis-decr")
    public ResponseEntity<Void> usingRedisDecr(@RequestBody UserCouponIssueRequest request) {
        userCouponService.issueCouponUsingRedisDECR(request.getUserId(), request.getCouponId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
