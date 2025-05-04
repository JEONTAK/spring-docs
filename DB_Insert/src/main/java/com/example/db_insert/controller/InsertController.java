package com.example.db_insert.controller;

import com.example.db_insert.dto.request.InsertRequest;
import com.example.db_insert.dto.response.InsertResponse;
import com.example.db_insert.service.InsertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "DB INSERT API", description = "1. Spring Data JPA 사용" +
        "2. JDBC Bulk Insert 사용" +
        "3. Spring Batch 사용" +
        "4.Spring Data JDBC 사용" +
        "을 할 수 있습니다.")
public class InsertController {

    private final InsertService insertService;

    @Operation(summary = "Spring Data JPA 사용", description = "Spring Data JPA 사용 API입니다")
    @PostMapping("/springdatajpa")
    public ResponseEntity<InsertResponse> saveUsingSpringDataJPA(@RequestBody InsertRequest insertRequest) {
        return ResponseEntity.ok(insertService.saveUsingSpringDataJPA(insertRequest.getAmount()));
    }

    @Operation(summary = "JDBC Bulk Insert 사용", description = "JDBC Bulk Insert 사용 API입니다")
    @PostMapping("/jdbc")
    public ResponseEntity<InsertResponse> saveUsingJDBC(@RequestBody InsertRequest insertRequest) {
        return ResponseEntity.ok(insertService.saveUsingJDBC(insertRequest.getAmount()));
    }

    @Operation(summary = "Simple JDBC Insert 사용", description = "Simple JDBC Insert API입니다")
    @PostMapping("/simplejdbc")
    public ResponseEntity<InsertResponse> savUsingSimpleJdbcInsert(@RequestBody InsertRequest insertRequest) {
        return ResponseEntity.ok(insertService.saveUsingSimpleJdbcInsert(insertRequest.getAmount()));
    }

    @Operation(summary = "Spring Data JDBC 사용", description = "Spring Data JDBC 사용 API입니다")
    @PostMapping("/springdatajdbc")
    public ResponseEntity<InsertResponse> saveUsingSpringDataJDBC(@RequestBody InsertRequest insertRequest) {
        return ResponseEntity.ok(insertService.saveUsingSpringDataJDBC(insertRequest.getAmount()));
    }
}
