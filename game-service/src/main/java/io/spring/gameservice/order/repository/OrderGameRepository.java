package io.spring.gameservice.order.repository;

import io.spring.gameservice.order.entity.Order;
import io.spring.gameservice.order.entity.OrderGame;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderGameRepository extends JpaRepository<OrderGame, Long> {
    List<OrderGame> findAllByOrderIn(List<Order> list);
}
