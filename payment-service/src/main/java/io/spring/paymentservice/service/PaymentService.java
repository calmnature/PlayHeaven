package io.spring.paymentservice.service;

import io.spring.paymentservice.constant.PaymentStatus;
import io.spring.paymentservice.dto.PaymentRequestDto;
import io.spring.paymentservice.dto.PaymentResponseDto;
import io.spring.paymentservice.entity.Payment;
import io.spring.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentResponseDto payment(PaymentRequestDto paymentRequestDto) {
        Payment payment = paymentRepository.save(
                Payment.builder()
                        .totalPrice(paymentRequestDto.getTotalPrice())
                        .paymentWay(paymentRequestDto.getPaymentWay())
                        .paymentStatus(PaymentStatus.COMPLETED)
                        .orderId(paymentRequestDto.getOrderId())
                        .build()
        );

        return PaymentResponseDto.builder()
                .success(true)
                .paymentId(payment.getPaymentId())
                .orderId(payment.getOrderId())
                .message("결제에 성공하였습니다.")
                .paymentWay(payment.getPaymentWay())
                .status(payment.getPaymentStatus().toString())
                .build();
    }
}
