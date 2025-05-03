package com.example.db_insert.dto.response;

import com.example.db_insert.service.InsertService;
import lombok.Getter;

@Getter
public class InsertResponse {

    private final Integer amount;
    private final String processTime;

    private InsertResponse(Integer amount, String processTime) {
        this.amount = amount;
        this.processTime = processTime;
    }

    public static InsertResponse of(int amount, String processTime) {
        return new InsertResponse(amount, processTime);
    }
}
