package com.example.concurrent.domain.coupon.entity;

import com.example.concurrent.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "coupons")
@Getter
@NoArgsConstructor
public class Coupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String couponName;

    @Column(nullable = false)
    private Long discountPrice;

    @Column(nullable = false)
    private Integer totalAmount;

    @Column(nullable = false)
    private Integer issuedAmount;

    public Coupon(String couponName, Long discountPrice, Integer totalAmount, Integer issuedAmount) {
        this.couponName = couponName;
        this.discountPrice = discountPrice;
        this.totalAmount = totalAmount;
        this.issuedAmount = issuedAmount;
    }

    public static Coupon create(String couponName, Long discountPrice, Integer totalAmount, Integer issuedAmount) {
        return new Coupon(couponName, discountPrice, totalAmount, issuedAmount);
    }

}
