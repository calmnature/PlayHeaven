package io.spring.paymentservice.controller;

import io.spring.paymentservice.dto.PaymentRequestDto;
import io.spring.paymentservice.dto.PaymentResponseDto;
import io.spring.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping(value = "/payment")
    public PaymentResponseDto payment(@RequestBody PaymentRequestDto paymentRequestDto){
        log.info("Feign Client 요청은 완료");
        return paymentService.payment(paymentRequestDto);
    }
}
