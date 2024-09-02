package io.spring.orderservice.payment.dto;

import io.spring.orderservice.payment.constant.PaymentWay;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentResponseDto {
    private boolean success;
    private Long paymentId;
    private Long orderId;
    private String message;
    private PaymentWay paymentWay;
    private String status;
}
