package io.spring.orderservice.wishlist.dto;

import io.spring.orderservice.common.dto.GameDto;
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
    private List<GameDto> wishlist;
}
