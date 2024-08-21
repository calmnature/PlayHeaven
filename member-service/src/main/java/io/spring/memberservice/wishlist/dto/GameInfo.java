package io.spring.memberservice.wishlist.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GameInfo {
    private Long gameId;
    private String gameName;
    private int price;

}
