package com.example.db_search.domain.order.entity;

import com.example.db_search.common.entity.BaseEntity;
import com.example.db_search.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Order(Long price, User user) {
        this.price = price;
        this.user = user;
    }

    public static Order create(Long price, User user) {
        return new Order(price, user);
    }
}
