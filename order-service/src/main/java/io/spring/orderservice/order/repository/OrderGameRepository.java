// TODO : 중간 테이블이 필요 없을 것 같음

package io.spring.orderservice.order.repository;

import io.spring.orderservice.order.entity.Order;
import io.spring.orderservice.order.entity.OrderGame;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderGameRepository extends JpaRepository<OrderGame, Long> {
    List<OrderGame> findAllByOrderIdIn(List<Long> orderIdlist);
}
