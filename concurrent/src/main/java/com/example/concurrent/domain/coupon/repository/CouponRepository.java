package com.example.concurrent.domain.coupon.repository;

import com.example.concurrent.domain.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    boolean existsByCouponName(String couponName);
}
