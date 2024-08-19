package io.spring.playheaven.ordergame.repository;

import io.spring.playheaven.order.entity.Order;
import io.spring.playheaven.ordergame.entity.OrderGame;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderGameRepository extends JpaRepository<OrderGame, Long> {
    List<OrderGame> findAllByOrderIn(List<Order> list);
}
