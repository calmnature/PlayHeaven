package io.spring.orderservice.common.feign.api;

import io.spring.orderservice.payment.dto.PaymentRequestDto;
import io.spring.orderservice.payment.dto.PaymentResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "PaymentApi", url = "http://localhost:9999/payment-service/v1")
public interface PaymentApi {
    @PostMapping("/payment")
    PaymentResponseDto payment(@RequestBody PaymentRequestDto paymentRequestDto);
}
