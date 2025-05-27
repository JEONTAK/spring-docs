package com.example.concurrent.domain.usercoupon.optimisticlock.repository;

import com.example.concurrent.domain.usercoupon.optimisticlock.entity.UserCouponOpt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCouponOptRepository extends JpaRepository<UserCouponOpt, Long> {
    boolean existsByUserIdAndCouponId(Long userId, Long couponId);

}
