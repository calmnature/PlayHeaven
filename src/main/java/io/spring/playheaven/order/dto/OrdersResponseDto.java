package io.spring.playheaven.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrdersResponseDto {
    private Long ordersId;
    private String ordersNumber;
    private int totalPrice;
    private List<GameInfo> gameList;
}
