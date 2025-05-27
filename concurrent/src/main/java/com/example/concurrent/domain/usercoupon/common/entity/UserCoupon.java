package com.example.concurrent.domain.usercoupon.common.entity;

import com.example.concurrent.common.entity.BaseEntity;
import com.example.concurrent.domain.coupon.entity.Coupon;
import com.example.concurrent.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usercoupons")
@Getter
@NoArgsConstructor
public class UserCoupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private UserCoupon(Coupon coupon, User user) {
        this.coupon = coupon;
        this.user = user;
    }

    public static UserCoupon create(Coupon coupon, User user) {
        return new UserCoupon(coupon, user);
    }
}
