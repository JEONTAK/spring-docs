package com.example.concurrent.domain.usercoupon.common.repository;

import com.example.concurrent.domain.usercoupon.common.entity.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {
    boolean existsByUserIdAndCouponId(Long userId, Long couponId);

}
