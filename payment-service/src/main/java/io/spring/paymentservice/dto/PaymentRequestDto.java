package io.spring.paymentservice.dto;

import io.spring.paymentservice.constant.PaymentWay;
import lombok.Getter;

@Getter
public class PaymentRequestDto {
    private int totalPrice;
    private PaymentWay paymentWay;
    private Long orderId;
}