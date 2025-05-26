package com.example.concurrent.domain.usercoupon.pessimisticlock.controller;

import com.example.concurrent.domain.usercoupon.dto.request.UserCouponIssueRequest;
import com.example.concurrent.domain.usercoupon.pessimisticlock.service.UserCouponPesService;
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
public class UserCouponPesController {

    private final UserCouponPesService userCouponPesService;

    //쿠폰 발급 (MySQL Pessimistic Lock)
    @PostMapping("/pessimistic-lock")
    public ResponseEntity<Void> usingPessimisticLock(@RequestBody UserCouponIssueRequest request) {
        userCouponPesService.issueCouponUsingPessimisticLock(request.getUserId(), request.getCouponId());
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
