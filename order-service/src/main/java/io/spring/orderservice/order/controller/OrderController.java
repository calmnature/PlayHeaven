package io.spring.orderservice.order.controller;

import io.spring.orderservice.order.dto.OrderRequestDto;
import io.spring.orderservice.order.dto.OrderResponseDto;
import io.spring.orderservice.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage/order")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/order")
    public ResponseEntity<String> purchase(@RequestBody OrderRequestDto ordersRequestDto){
        boolean success = orderService.purchase(ordersRequestDto);
        return success ? ResponseEntity.status(HttpStatus.OK).body("구매가 완료되었습니다.") :
                        ResponseEntity.status(HttpStatus.BAD_REQUEST).body("구매가 실패되었습니다.");
    }

    @GetMapping("/list/{memberId}")
    public ResponseEntity<List<OrderResponseDto>> allList(@PathVariable(name = "memberId")Long memberId,
                                                          @RequestParam(name = "pageNo", defaultValue = "1")int pageNo,
                                                          @RequestParam(name = "size", defaultValue = "10")int size){
        List<OrderResponseDto> responseDtoList = orderService.allList(memberId, pageNo - 1, size);

        return !responseDtoList.isEmpty() ? ResponseEntity.status(HttpStatus.OK).body(responseDtoList) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("/refund")
    public ResponseEntity<String> refund(@RequestParam(name = "memberId")Long memberId,
                                         @RequestParam(name = "orderId")Long orderId){
        boolean success = orderService.refund(memberId, orderId);
        return success ? ResponseEntity.status(HttpStatus.OK).body("환불 처리가 완료되었습니다.") :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 환불 처리가 되었거나 구매 24시간이 경과되었습니다.\n구매 확정이 되었다면 환불이 불가합니다.");
    }
}
