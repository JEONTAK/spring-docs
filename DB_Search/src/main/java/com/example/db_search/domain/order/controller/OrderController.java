package com.example.db_search.domain.order.controller;

import com.example.db_search.common.annotation.ResponseTime;
import com.example.db_search.domain.order.dto.response.OrderResponse;
import com.example.db_search.domain.order.entity.Order;
import com.example.db_search.domain.order.service.OrderCommandService;
import com.example.db_search.domain.order.service.OrderQueryService;
import com.example.db_search.domain.user.dto.response.UserWithOrderResponse;
import com.example.db_search.domain.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "주문 API", description = "주문 삽입 및 다양한 조회 관련 API")
@ResponseTime
public class OrderController {

    private final OrderCommandService orderCommandService;
    private final OrderQueryService orderQueryService;

    @Operation(summary = "1. 주문 삽입", description = "100만건의 주문을 랜덤으로 INSERT 합니다.")
    @PostMapping("/init")
    public ResponseEntity<Void> initOrders(){
        orderCommandService.initOrders();
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @Operation(summary = "2. 기본 주문 조회", description = "주문과 그에 대한 유저 정보를 조회합니다. N + 1 문제가 발생합니다.")
    @GetMapping("/price/default")
    public ResponseEntity<List<UserWithOrderResponse>> getUsersAndOrdersUsingDefault(@RequestParam(name = "price", defaultValue = "99000") Long price) {
        List<Order> orders = orderQueryService.findAllWithUser(price);
        Map<User, List<Order>> orderByUser = orders.stream().collect(Collectors.groupingBy(Order::getUser));
        List<UserWithOrderResponse> responses = orderByUser.entrySet().stream()
                .map(entry -> {
                    User user = entry.getKey();

                    List<OrderResponse> orderResponses = entry.getValue().stream().map(order -> {
                        return OrderResponse.create(order.getId(), order.getPrice());
                    }).toList();

                    return UserWithOrderResponse.create(
                            user.getId(),
                            user.getEmail(),
                            user.getName(),
                            user.getAge(),
                            orderResponses
                    );
                }).toList();
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "3. JOIN FETCH를 사용한 주문 조회", description = "주문과 그에 대한 유저 정보를 조회합니다. N + 1 문제가 발생하지 않습니다")
    @GetMapping("/price/joinfetch")
    public ResponseEntity<List<UserWithOrderResponse>> getUsersAndOrdersUsingJoinFetch(@RequestParam(name = "price", defaultValue = "99000") Long price) {
        List<Order> orders = orderQueryService.findAllUsingJoinFetch(price);
        Map<User, List<Order>> orderByUser = orders.stream().collect(Collectors.groupingBy(Order::getUser));
        List<UserWithOrderResponse> responses = orderByUser.entrySet().stream()
                .map(entry -> {
                    User user = entry.getKey();

                    List<OrderResponse> orderResponses = entry.getValue().stream().map(order -> {
                        return OrderResponse.create(order.getId(), order.getPrice());
                    }).toList();

                    return UserWithOrderResponse.create(
                            user.getId(),
                            user.getEmail(),
                            user.getName(),
                            user.getAge(),
                            orderResponses
                    );
                }).toList();
        return ResponseEntity.ok(responses);
    }
}
