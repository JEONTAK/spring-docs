package com.example.concurrent.domain.usercoupon.optimisticlock.entity;

import com.example.concurrent.common.entity.BaseEntity;
import com.example.concurrent.domain.coupon.entity.Coupon;
import com.example.concurrent.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usercouponopts")
@Getter
@NoArgsConstructor
public class UserCouponOpt extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Version
    private Long version;

    private UserCouponOpt(Coupon coupon, User user) {
        this.coupon = coupon;
        this.user = user;
    }

    public static UserCouponOpt create(Coupon coupon, User user) {
        return new UserCouponOpt(coupon, user);
    }
}
