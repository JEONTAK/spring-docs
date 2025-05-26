package com.example.concurrent.domain.usercoupon.optimisticlock.repository;

import com.example.concurrent.domain.usercoupon.entity.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCouponOptRepository extends JpaRepository<UserCoupon, Long> {
    boolean existsByUserIdAndCouponId(Long userId, Long couponId);

}
