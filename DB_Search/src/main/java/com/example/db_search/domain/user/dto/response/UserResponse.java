package com.example.db_search.domain.user.dto.response;

import lombok.Getter;

@Getter
public class UserResponse {

    private final Long id;
    private final String email;
    private final String name;
    private final Integer age;

    private UserResponse(Long id, String email, String name, Integer age) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.age = age;
    }

    public static UserResponse create(Long id, String email, String name, Integer age) {
        return new UserResponse(id, email, name, age);
    }
}
