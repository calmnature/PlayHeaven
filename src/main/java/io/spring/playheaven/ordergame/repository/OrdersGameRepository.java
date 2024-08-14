package io.spring.playheaven.ordergame.repository;

import io.spring.playheaven.ordergame.entity.OrdersGame;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersGameRepository extends JpaRepository<OrdersGame, Long> {
}
