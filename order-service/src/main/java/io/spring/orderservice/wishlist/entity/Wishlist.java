package io.spring.orderservice.wishlist.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wishlist {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wishlistId;

    @Setter
    private int totalPrice;

    private Long memberId;
}
