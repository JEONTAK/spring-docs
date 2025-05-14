package com.example.db_search.domain.order.repository;

import com.example.db_search.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o JOIN FETCH o.user WHERE o.price = :price")
    List<Order> findAllWithUserUsingJoinFetch(Long price);

    List<Order> findAllByPrice(Long Price);
}
