package com.example.db_search.domain.user.dto.response;

import lombok.Getter;

@Getter
public class UserQueryResponse {

    private final Long id;
    private final String email;

    private UserQueryResponse(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public static UserQueryResponse create(Long id, String email) {
        return new UserQueryResponse(id, email);
    }
}
