package io.spring.orderservice.order.dto;

import io.spring.orderservice.order.constant.OrderStatus;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderResponseDto {
    private Long orderId;
    private String orderNumber;
    private int totalPrice;
    private OrderStatus orderStatus;
    private List<GameDto> gameList;
}
