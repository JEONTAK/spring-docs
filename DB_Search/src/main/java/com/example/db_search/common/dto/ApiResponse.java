package com.example.db_search.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ApiResponse<T> {

    @Schema(description = "응답 시간 (밀리초)", example = "50")
    private final String responseTimeMs;
    @Schema(description = "응답 데이터")
    private final T data;

    private ApiResponse(String responseTimeMs, T data) {
        this.responseTimeMs = responseTimeMs;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(double responseTimeMs, T data) {
        return new ApiResponse<>(responseTimeMs + "초", data);
    }
}