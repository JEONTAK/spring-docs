package com.example.concurrent.domain.usercoupon.pessimisticlock.entity;

import com.example.concurrent.common.entity.BaseEntity;
import com.example.concurrent.domain.coupon.entity.Coupon;
import com.example.concurrent.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usercoupons_pes")
@Getter
@NoArgsConstructor
public class UserCouponPes extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private UserCouponPes(Coupon coupon, User user) {
        this.coupon = coupon;
        this.user = user;
    }

    public static UserCouponPes create(Coupon coupon, User user) {
        return new UserCouponPes(coupon, user);
    }
}
