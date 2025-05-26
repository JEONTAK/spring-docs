package com.example.concurrent.domain.usercoupon.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserCouponIssueRequest {

    @Schema(description = "유저 아이디", example = "1")
    private Long userId;

    @Schema(description = "쿠폰 아이디", example = "1")
    private Long couponId;
}
