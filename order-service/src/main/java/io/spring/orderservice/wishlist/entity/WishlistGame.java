// TODO : 중간 테이블 필요 없을 거 같음?

package io.spring.orderservice.wishlist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishlistGame {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wishlistGameId;
    private int price;

    private Long wishlistId;

    private Long gameId;
}
