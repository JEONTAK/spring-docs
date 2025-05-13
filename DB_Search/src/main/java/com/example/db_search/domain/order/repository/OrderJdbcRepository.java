package com.example.db_search.domain.order.repository;

import com.example.db_search.common.consts.Const;
import com.example.db_search.domain.order.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public void saveAll(List<Order> orders) {
        String sql = """
                INSERT INTO orders(price, user_id, created_at, modified_at)
                VALUES (?, ?, ?, ?)
                """;

        jdbcTemplate.batchUpdate(sql, orders, Const.BATCH_SIZE, (PreparedStatement ps, Order order) -> {
            ps.setLong(1, order.getPrice());
            ps.setLong(2, order.getUser().getId());
            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
        });
    }
}
