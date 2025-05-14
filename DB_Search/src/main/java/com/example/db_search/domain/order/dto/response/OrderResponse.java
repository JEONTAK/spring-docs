package com.example.db_search.domain.order.dto.response;

import lombok.Getter;

@Getter
public class OrderResponse {

    private final Long id;
    private final Long price;

    private OrderResponse(Long id, Long price) {
        this.id = id;
        this.price = price;
    }

    public static OrderResponse create(Long id, Long price) {
        return new OrderResponse(id, price);
    }
}
