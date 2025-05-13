package com.example.db_search.domain.user.controller;

import com.example.db_search.domain.user.service.UserCommandService;
import com.example.db_search.domain.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    @PostMapping("/init")
    public ResponseEntity<Void> initUsers(){
        userCommandService.initUsers();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
