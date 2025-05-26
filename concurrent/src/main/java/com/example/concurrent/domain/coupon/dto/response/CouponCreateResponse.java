package com.example.concurrent.domain.coupon.dto.response;

import lombok.Getter;

@Getter
public class CouponCreateResponse {

    private final String message;

    private CouponCreateResponse(String message) {
        this.message = message;
    }

    public static CouponCreateResponse of(String message) {
        return new CouponCreateResponse(message);
    }
}
