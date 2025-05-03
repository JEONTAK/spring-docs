package com.example.db_insert.service;

import com.example.db_insert.dto.response.InsertResponse;
import com.example.db_insert.entity.User;
import com.example.db_insert.repository.InsertRepository;
import com.example.db_insert.repository.JdbcRepository;
import com.example.db_insert.repository.SpringJdbcRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional
public class InsertService {

    private final InsertRepository insertRepository;
    private final JdbcRepository jdbcRepository;
    private final SpringJdbcRepository springJdbcRepository;

    public InsertResponse saveUsingSpringDataJPA(Integer amount) {
        double startTime = System.currentTimeMillis();
        List<User> users = IntStream.range(0, amount).mapToObj(i -> "example" + LocalDateTime.now().getNano()).map(User::create).toList();
        insertRepository.saveAll(users);
        double endTime = System.currentTimeMillis();
        return InsertResponse.of(amount, ((endTime - startTime) / 1000) + "초");
    }

    public InsertResponse saveUsingJDBC(Integer amount) {
        double startTime = System.currentTimeMillis();
        List<User> users = IntStream.range(0, amount).mapToObj(i -> "example" + LocalDateTime.now().getNano()).map(User::create).toList();
        jdbcRepository.saveUsingJDBC(users);
        double endTime = System.currentTimeMillis();
        return InsertResponse.of(amount, ((endTime - startTime) / 1000) + "초");
    }

    public InsertResponse simpleJdbcInsertBatch(Integer amount) {
        double startTime = System.currentTimeMillis();
        List<User> users = IntStream.range(0, amount).mapToObj(i -> "example" + LocalDateTime.now().getNano()).map(User::create).toList();
        jdbcRepository.saveUsingSimpleJDBC(users);
        double endTime = System.currentTimeMillis();
        return InsertResponse.of(amount, ((endTime - startTime) / 1000) + "초");
    }

    public InsertResponse saveUsingSpringDataJDBC(Integer amount) {
        double startTime = System.currentTimeMillis();
        List<User> users = IntStream.range(0, amount).mapToObj(i -> "example" + LocalDateTime.now().getNano()).map(User::create).toList();
        springJdbcRepository.saveAll(users);
        double endTime = System.currentTimeMillis();
        return InsertResponse.of(amount, ((endTime - startTime) / 1000) + "초");
    }
}
