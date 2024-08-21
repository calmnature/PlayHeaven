package io.spring.orderservice.wishlist.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class WishlistResponseDto {
    private Long wishlistId;
    private int totalPrice;
    private List<GameInfo> wishlist;
}
