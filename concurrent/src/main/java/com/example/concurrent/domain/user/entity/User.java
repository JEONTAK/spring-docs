package com.example.concurrent.domain.user.entity;

import com.example.concurrent.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private Integer age;

    private User(String username, Integer age) {
        this.username = username;
        this.age = age;
    }

    public static User create(String username, Integer age) {
        return new User(username, age);
    }
}
