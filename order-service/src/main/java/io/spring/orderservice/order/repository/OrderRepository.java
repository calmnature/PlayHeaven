package io.spring.orderservice.order.repository;

import io.spring.orderservice.order.constant.OrderStatus;
import io.spring.orderservice.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByMember_MemberId(Long memberId, Pageable pageable);

    List<Order> findAllByOrderStatus(OrderStatus orderStatus);
}
