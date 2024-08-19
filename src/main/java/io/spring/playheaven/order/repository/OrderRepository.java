package io.spring.playheaven.order.repository;

import io.spring.playheaven.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByMember_MemberId(Long memberId, Pageable pageable);
}
