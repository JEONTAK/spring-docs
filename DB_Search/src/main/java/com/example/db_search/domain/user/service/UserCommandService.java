package com.example.db_search.domain.user.service;

import com.example.db_search.common.consts.Const;
import com.example.db_search.common.exception.CustomException;
import com.example.db_search.domain.user.entity.User;
import com.example.db_search.domain.user.repository.UserJdbcRepository;
import com.example.db_search.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class UserCommandService {

    private final UserRepository userRepository;
    private final UserJdbcRepository userJdbcRepository;

    public void initUsers() {
        if (userRepository.count() != 0) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "이미 유저가 생성되어있습니다.");
        }

        List<User> users = new ArrayList<>();
        for (int i = 0; i < Const.TOTAL_RECORDS; i++) {
            String email = "user_" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
            String name = "Name_" + UUID.randomUUID().toString().substring(0, 8);
            int age = 18 + ThreadLocalRandom.current().nextInt(82);
            users.add(User.create(email, name, age));

            if (users.size() >= Const.BATCH_SIZE) {
                userJdbcRepository.saveAll(users);
                users.clear();
                log.info("유저 생성 완료한 개수 : " + (i + 1));
            }
        }
        if (!users.isEmpty()) {
            userRepository.saveAll(users);
        }
    }

}
