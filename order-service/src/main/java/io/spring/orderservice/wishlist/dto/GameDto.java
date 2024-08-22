package io.spring.orderservice.wishlist.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GameDto {
    private Long gameId;
    private String gameName;
    private int price;
    private int eventStock;
    private String detail;
    private boolean saled;
    private Long memberId;
}
