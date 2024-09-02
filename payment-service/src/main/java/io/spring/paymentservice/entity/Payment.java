package io.spring.paymentservice.entity;

import io.spring.paymentservice.constant.PaymentStatus;
import io.spring.paymentservice.constant.PaymentWay;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment extends BaseTime{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;
    private int totalPrice;
    @Enumerated(EnumType.STRING)
    private PaymentWay paymentWay;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    private String detail;
    private Long orderId;
}
