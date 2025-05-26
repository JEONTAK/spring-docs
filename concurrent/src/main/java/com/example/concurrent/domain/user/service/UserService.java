package com.example.concurrent.domain.user.service;

import com.example.concurrent.common.consts.Const;
import com.example.concurrent.common.exception.CustomException;
import com.example.concurrent.domain.user.dto.response.UserCreateResponse;
import com.example.concurrent.domain.user.entity.User;
import com.example.concurrent.domain.user.repository.UserJdbcRepository;
import com.example.concurrent.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserJdbcRepository userJdbcRepository;

    public UserCreateResponse createUsers(Integer amount) {
        if (userRepository.count() != 0) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "이미 유저가 생성되어있습니다.");
        }

        List<User> users = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            String name = "Name_" + UUID.randomUUID().toString().substring(0, 8);
            int age = 18 + ThreadLocalRandom.current().nextInt(82);
            users.add(User.create(name, age));

            if (users.size() >= Const.BATCH_SIZE) {
                userJdbcRepository.saveAll(users);
                users.clear();
            }
        }
        if (!users.isEmpty()) {
            userRepository.saveAll(users);
        }

        return UserCreateResponse.of(amount + "개 유저 생성에 성공하였습니다.");
    }
}
