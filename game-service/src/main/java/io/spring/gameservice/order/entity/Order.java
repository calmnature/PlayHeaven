package io.spring.gameservice.order.entity;

import io.spring.gameservice.member.entity.Member;
import io.spring.gameservice.order.constant.OrderStatus;
import io.spring.gameservice.order.dto.OrderRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "Orders")
public class Order extends BaseTime {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private String orderNumber;

    private int totalPrice;

    @Setter
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static Order toEntity (OrderRequestDto ordersRequestDto) {
        return new Order(
                null,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")),
                ordersRequestDto.getTotalPrice(),
                OrderStatus.PURCHASE,
                Member.builder().memberId(ordersRequestDto.getMemberId()).build()
        );
    }

}
