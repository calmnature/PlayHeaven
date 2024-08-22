// TODO : 중간 테이블이 필요 없을 것 같음

package io.spring.orderservice.order.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderGame {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderGameId;
    private int price;
    private Long orderId;
    private Long gameId;
}
