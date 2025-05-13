package com.example.db_search.domain.order.controller;

import com.example.db_search.domain.order.service.OrderCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderCommandService orderCommandService;

    @PostMapping("/init")
    public ResponseEntity<Void> initOrders(){
        orderCommandService.initOrders();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
