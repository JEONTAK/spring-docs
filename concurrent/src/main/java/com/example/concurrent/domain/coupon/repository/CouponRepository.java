package com.example.concurrent.domain.coupon.repository;

import com.example.concurrent.domain.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    boolean existsByCouponName(String couponName);

    @Modifying
    @Query("UPDATE Coupon c SET c.issuedAmount = c.issuedAmount + 1 WHERE c.id = :id AND c.issuedAmount < c.totalAmount")
    int incrementIssuedAmount(@Param("id") Long id);

    @Query(value = "SELECT GET_LOCK(:lockName, :timeout)", nativeQuery = true)
    int getLock(@Param("lockName") String lockName, @Param("timeout") int timeout);

    @Query(value = "SELECT RELEASE_LOCK(:lockName)", nativeQuery = true)
    int releaseLock(@Param("lockName") String lockName);
}
