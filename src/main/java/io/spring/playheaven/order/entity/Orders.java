package io.spring.playheaven.order.entity;

import io.spring.playheaven.member.entity.Member;
import io.spring.playheaven.order.constant.OrdersStatus;
import io.spring.playheaven.order.dto.OrdersRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Orders extends BaseTime {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ordersId;

    private String orderNumber;

    private int totalPrice;

    @Enumerated(EnumType.STRING)
    private OrdersStatus orderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static Orders toEntity (OrdersRequestDto ordersRequestDto) {
        return new Orders(
                null,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")),
                ordersRequestDto.getTotalPrice(),
                OrdersStatus.PURCHASE,
                Member.builder().memberId(ordersRequestDto.getMemberId()).build()
        );
    }
}
