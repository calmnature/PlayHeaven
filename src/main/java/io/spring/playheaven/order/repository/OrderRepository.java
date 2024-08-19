package io.spring.playheaven.order.repository;

import io.spring.playheaven.order.constant.OrderStatus;
import io.spring.playheaven.order.entity.Order;
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
