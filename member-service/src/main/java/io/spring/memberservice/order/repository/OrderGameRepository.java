package io.spring.memberservice.order.repository;

import io.spring.memberservice.order.entity.Order;
import io.spring.memberservice.order.entity.OrderGame;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderGameRepository extends JpaRepository<OrderGame, Long> {
    List<OrderGame> findAllByOrderIn(List<Order> list);
}
