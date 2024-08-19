package io.spring.playheaven.ordersgame.repository;

import io.spring.playheaven.order.entity.Orders;
import io.spring.playheaven.ordersgame.entity.OrdersGame;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersGameRepository extends JpaRepository<OrdersGame, Long> {
    List<OrdersGame> findAllByOrdersIn(List<Orders> list);
}
