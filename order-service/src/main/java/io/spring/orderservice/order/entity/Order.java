package io.spring.orderservice.order.entity;

import io.spring.orderservice.order.constant.OrderStatus;
import io.spring.orderservice.order.dto.OrderRequestDto;
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

    private Long memberId;

    public static Order toEntity (int totalPrice, Long memberId) {
        return new Order(
                null,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")),
                totalPrice,
                OrderStatus.PURCHASE,
                memberId
        );
    }

}
