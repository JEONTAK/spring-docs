package com.example.db_search.domain.user.dto.response;

import com.example.db_search.domain.order.dto.response.OrderResponse;
import lombok.Getter;

import java.util.List;

@Getter
public class UserWithOrderResponse {

    private final Long id;
    private final String email;
    private final String name;
    private final Integer age;
    private final List<OrderResponse> orders;

    private UserWithOrderResponse(Long id, String email, String name, Integer age, List<OrderResponse> orders) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.age = age;
        this.orders = orders;
    }

    public static UserWithOrderResponse create(Long id, String email, String name, Integer age, List<OrderResponse> orders) {
        return new UserWithOrderResponse(id, email, name, age, orders);
    }
}
