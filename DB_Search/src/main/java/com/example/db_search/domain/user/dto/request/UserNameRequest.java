package com.example.db_search.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class UserNameRequest {

    @Schema(description = "이름", example = "Name_0")
    private String name;

}
