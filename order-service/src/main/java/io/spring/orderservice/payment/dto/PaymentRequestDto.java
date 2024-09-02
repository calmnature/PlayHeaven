package io.spring.orderservice.payment.dto;

import io.spring.orderservice.payment.constant.PaymentWay;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentRequestDto {
    private int totalPrice;
    private PaymentWay paymentWay;
    private Long orderId;
}