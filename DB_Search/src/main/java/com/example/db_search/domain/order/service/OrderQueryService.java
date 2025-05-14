package com.example.db_search.domain.order.service;

import com.example.db_search.domain.order.entity.Order;
import com.example.db_search.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderQueryService {

    private final OrderRepository orderRepository;
    
    //Fetch Join 조회
    public List<Order> findAllUsingJoinFetch(Long price) {
        return orderRepository.findAllWithUserUsingJoinFetch(price);
    }

    // 기본 조회(연관된 User 함께 조회)
    public List<Order> findAllWithUser(Long price) {
        return orderRepository.findAllByPrice(price);
    }
}
