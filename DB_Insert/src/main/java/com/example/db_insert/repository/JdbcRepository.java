package com.example.db_insert.repository;

import com.example.db_insert.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class JdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public void saveUsingJDBC(List<User> users) {
        String sql = """
                INSERT INTO users(name)
                   VALUES(?)
                """;

        jdbcTemplate.batchUpdate(sql, users, 1000, (PreparedStatement ps, User user) -> {
            ps.setString(1, user.getName());
        });
    }

    public void saveUsingSimpleJDBC(List<User> users){
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingColumns("name");

        List<Map<String, Object>> batchParams = new ArrayList<>();
        int batchSize = 1000;

        for (User user : users) {
            Map<String, Object> params = new HashMap<>();
            params.put("name", user.getName());
            batchParams.add(params);

            if (batchParams.size() == batchSize || batchParams.size() == users.size()) {
                simpleJdbcInsert.executeBatch(batchParams.toArray(new Map[0]));
                batchParams.clear();
            }
        }

        if (!batchParams.isEmpty()) {
            simpleJdbcInsert.executeBatch(batchParams.toArray(new Map[0]));
        }
    }
}
