package io.spring.playheaven.order.dto;

import io.spring.playheaven.order.constant.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderResponseDto {
    private Long orderId;
    private String ordersNumber;
    private int totalPrice;
    private OrderStatus ordersStatus;
    private List<GameInfo> gameList;
}
