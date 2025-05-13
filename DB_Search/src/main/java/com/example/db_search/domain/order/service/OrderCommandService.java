package com.example.db_search.domain.order.service;

import com.example.db_search.common.consts.Const;
import com.example.db_search.common.exception.CustomException;
import com.example.db_search.domain.order.entity.Order;
import com.example.db_search.domain.order.repository.OrderJdbcRepository;
import com.example.db_search.domain.order.repository.OrderRepository;
import com.example.db_search.domain.user.entity.User;
import com.example.db_search.domain.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderCommandService {

    private final OrderRepository orderRepository;
    private final OrderJdbcRepository orderJdbcRepository;
    private final UserQueryService userQueryService;

    public void initOrders() {
        List<User> allUsers = userQueryService.findAll();
        if (allUsers.isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "현재 유저가 존재하지 않습니다. 유저를 먼저 생성해주세요.");
        }

        if (orderRepository.count() != 0) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "이미 주문이 생성되어있습니다.");
        }

        List<Order> orders = new ArrayList<>();
        for (int i = 0; i < Const.TOTAL_RECORDS; i++) {
            long price = 1000 + (ThreadLocalRandom.current().nextLong(99) * 1000);
            User user = allUsers.get(ThreadLocalRandom.current().nextInt(allUsers.size()));
            orders.add(Order.create(price, user));

            if (orders.size() >= Const.BATCH_SIZE) {
                orderJdbcRepository.saveAll(orders);
                orders.clear();
                log.info("주문 생성 완료한 개수 : " + (i + 1));
            }
        }
        if (!orders.isEmpty()) {
            orderRepository.saveAll(orders);
        }
    }
}
