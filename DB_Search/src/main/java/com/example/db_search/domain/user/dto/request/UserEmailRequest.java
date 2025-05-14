package com.example.db_search.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class UserEmailRequest {

    @Schema(description = "이메일", example = "user_000")
    private String email;

}
