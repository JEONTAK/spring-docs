package com.example.db_search.domain.user.repository;

import com.example.db_search.common.consts.Const;
import com.example.db_search.domain.user.entity.User;
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
                INSERT INTO users(email, indexing_email, name, age, created_at, modified_at)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        jdbcTemplate.batchUpdate(sql, users, Const.BATCH_SIZE, (PreparedStatement ps, User user) -> {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getIndexingEmail());
            ps.setString(3, user.getName());
            ps.setInt(4, user.getAge());
            ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
        });
    }
}
