package com.example.db_search.domain.user.entity;

import com.example.db_search.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users", indexes = @Index(name = "idx_email", columnList = "indexingEmail"))
@Getter
@NoArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String indexingEmail;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer age;

    private User(String email, String indexingEmail, String name, Integer age) {
        this.email = email;
        this.indexingEmail = indexingEmail;
        this.name = name;
        this.age = age;
    }

    public static User create(String email, String indexingEmail, String name, Integer age) {
        return new User(email, indexingEmail, name, age);
    }
}
