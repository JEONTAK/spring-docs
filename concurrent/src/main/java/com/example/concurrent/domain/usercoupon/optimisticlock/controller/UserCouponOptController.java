package com.example.concurrent.domain.usercoupon.optimisticlock.controller;

import com.example.concurrent.domain.usercoupon.dto.request.UserCouponIssueRequest;
import com.example.concurrent.domain.usercoupon.optimisticlock.service.UserCouponOptService;
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
public class UserCouponOptController {

    private final UserCouponOptService userCouponOptService;

    //쿠폰 발급 (MySQL Optimistic Lock)
    @PostMapping("/optimistic-lock")
    public ResponseEntity<Void> usingOptimisticLock(@RequestBody UserCouponIssueRequest request) {
        userCouponOptService.issueCouponUsingOptimisticLock(request.getUserId(), request.getCouponId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
