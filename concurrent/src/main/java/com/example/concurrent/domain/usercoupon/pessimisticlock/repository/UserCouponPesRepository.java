package com.example.concurrent.domain.usercoupon.pessimisticlock.repository;

import com.example.concurrent.domain.coupon.entity.Coupon;
import com.example.concurrent.domain.usercoupon.pessimisticlock.entity.UserCouponPes;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserCouponPesRepository extends JpaRepository<UserCouponPes, Long> {
    boolean existsByUserIdAndCouponId(Long userId, Long couponId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Coupon c WHERE c.id = :id")
    Optional<Coupon> findByIdWithPessimisticLock(@Param("id") Long id);
}
