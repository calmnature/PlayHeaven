package io.spring.playheaven.order.controller;

import io.spring.playheaven.order.dto.OrdersRequestDto;
import io.spring.playheaven.order.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage/order")
public class OrdersController {
    private final OrdersService orderService;

    @PostMapping("/order")
    public ResponseEntity<String> purchase(@RequestBody OrdersRequestDto ordersRequestDto){
        boolean success = orderService.purchase(ordersRequestDto);
        return success ? ResponseEntity.status(HttpStatus.OK).body("구매가 완료되었습니다.") :
                        ResponseEntity.status(HttpStatus.BAD_REQUEST).body("구매가 실패되었습니다.");
    }
}
