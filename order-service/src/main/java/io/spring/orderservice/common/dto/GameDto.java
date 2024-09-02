package io.spring.orderservice.common.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class GameDto {
    private Long gameId;
    private String gameName;
    @Setter
    private int price;
    private int eventStock;
    private String detail;
    private boolean saled;
    private Long memberId;
}
