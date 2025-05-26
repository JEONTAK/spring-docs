package com.example.concurrent.domain.user.repository;

import com.example.concurrent.common.consts.Const;
import com.example.concurrent.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public void saveAll(List<User> users) {
        String sql = """
                INSERT INTO users(name, age, created_at, modified_at)
                VALUES (?, ?, ?, ?)
                """;

        jdbcTemplate.batchUpdate(sql, users, Const.BATCH_SIZE, (PreparedStatement ps, User user) -> {
            ps.setString(1, user.getUsername());
            ps.setInt(2, user.getAge());
            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
        });
    }
}
