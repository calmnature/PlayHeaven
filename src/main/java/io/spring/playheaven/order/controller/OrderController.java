package io.spring.playheaven.order.controller;

import io.spring.playheaven.order.dto.OrderRequestDto;
import io.spring.playheaven.order.service.OrderService;
import io.spring.playheaven.order.dto.OrderResponseDto;
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
}
