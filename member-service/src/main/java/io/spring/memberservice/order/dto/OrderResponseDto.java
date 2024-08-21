package io.spring.memberservice.order.dto;

import io.spring.memberservice.order.constant.OrderStatus;
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
    private String orderNumber;
    private int totalPrice;
    private OrderStatus orderStatus;
    private List<GameInfo> gameList;
}
