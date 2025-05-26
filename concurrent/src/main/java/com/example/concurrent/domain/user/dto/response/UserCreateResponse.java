package com.example.concurrent.domain.user.dto.response;

import lombok.Getter;

@Getter
public class UserCreateResponse {

    private final String message;

    private UserCreateResponse(String message) {
        this.message = message;
    }

    public static UserCreateResponse of(String message) {
        return new UserCreateResponse(message);
    }
}
