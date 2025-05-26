package com.example.concurrent.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserCreateRequest {

    @Schema(description = "삽입 유저 수", example = "1000")
    private Integer amount;
}
