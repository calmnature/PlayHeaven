package io.spring.memberservice.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GameInfo {
    private Long gameId;
    private String gameName;
    private int price;
}
