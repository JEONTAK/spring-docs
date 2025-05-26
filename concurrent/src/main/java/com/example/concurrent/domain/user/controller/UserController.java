package com.example.concurrent.domain.user.controller;

import com.example.concurrent.domain.user.dto.request.UserCreateRequest;
import com.example.concurrent.domain.user.dto.response.UserCreateResponse;
import com.example.concurrent.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "유저 API", description = "유저 API")
public class UserController {

    private final UserService userService;


    //유저 등록
    @Operation(summary = "유저 삽입", description = "요청한 수만큼 유저를 랜덤으로 INSERT 합니다.")
    @PostMapping("/create")
    public ResponseEntity<UserCreateResponse> createUsers(@RequestBody UserCreateRequest request) {
        UserCreateResponse response = userService.createUsers(request.getAmount());
        return ResponseEntity.ok(response);
    }


}
