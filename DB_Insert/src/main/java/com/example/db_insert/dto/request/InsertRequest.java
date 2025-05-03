package com.example.db_insert.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InsertRequest {

    @Schema(description = "삽입할 데이터 개수", example = "10000")
    @NotNull
    private Integer amount;

}
