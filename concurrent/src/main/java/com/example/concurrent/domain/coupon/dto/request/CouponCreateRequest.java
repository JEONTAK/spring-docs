package com.example.concurrent.domain.coupon.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CouponCreateRequest {

    @Schema(description = "쿠폰이름", example = "3000원 할인 쿠폰")
    private String couponName;

    @Schema(description = "총 수량", example = "100000")
    private Integer totalAmount;

    @Schema(description = "할인가격", example = "3000")
    private Long discountPrice;
}
