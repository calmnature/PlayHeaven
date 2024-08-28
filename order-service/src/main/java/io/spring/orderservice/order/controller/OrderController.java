package io.spring.orderservice.order.controller;

import io.spring.orderservice.order.dto.GameIdListDto;
import io.spring.orderservice.order.dto.OrderResponseDto;
import io.spring.orderservice.order.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/order")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/request")
    public ResponseEntity<String> requestOrder(@RequestBody GameIdListDto gameIdListDto,
                                               HttpServletRequest req){
        boolean success = orderService.requestOrder(gameIdListDto, req);
        return success ? ResponseEntity.status(HttpStatus.OK).body("주문이 완료되었습니다.") :
                        ResponseEntity.status(HttpStatus.BAD_REQUEST).body("주문에 실패하였습니다.");
    }

    @GetMapping("/list")
    public ResponseEntity<?> list(@RequestParam(name = "pageNo", defaultValue = "1")int pageNo,
                                                       @RequestParam(name = "size", defaultValue = "10")int size,
                                                       HttpServletRequest req){
        List<OrderResponseDto> responseDtoList = orderService.list(pageNo - 1, size, req);

        return !responseDtoList.isEmpty() ? ResponseEntity.status(HttpStatus.OK).body(responseDtoList) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("주문 조회에 실패하였습니다.");
    }

    @GetMapping("/refund/{orderId}")
    public ResponseEntity<String> refund(@PathVariable(name = "orderId")Long orderId,
                                         HttpServletRequest req) {
        boolean success = orderService.refund(orderId, req);
        return success ? ResponseEntity.status(HttpStatus.OK).body("환불 처리가 완료되었습니다.") :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 환불 처리가 되었거나 구매 24시간이 경과되었습니다.\n구매 확정이 되었다면 환불이 불가합니다.");
    }
}
