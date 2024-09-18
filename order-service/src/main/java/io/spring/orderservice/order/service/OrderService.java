package io.spring.orderservice.order.service;

import io.jsonwebtoken.Claims;
import io.spring.orderservice.common.dto.GameDto;
import io.spring.orderservice.common.feign.api.GameApi;
import io.spring.orderservice.common.feign.api.PaymentApi;
import io.spring.orderservice.common.jwt.JwtUtil;
import io.spring.orderservice.order.constant.OrderStatus;
import io.spring.orderservice.order.dto.GameIdListDto;
import io.spring.orderservice.order.dto.OrderResponseDto;
import io.spring.orderservice.payment.constant.PaymentWay;
import io.spring.orderservice.payment.dto.PaymentRequestDto;
import io.spring.orderservice.order.entity.Order;
import io.spring.orderservice.order.entity.OrderGame;
import io.spring.orderservice.order.repository.OrderGameRepository;
import io.spring.orderservice.order.repository.OrderRepository;
import io.spring.orderservice.payment.dto.PaymentResponseDto;
import io.spring.orderservice.redis.service.RedisService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderGameRepository orderGameRepository;
    private final GameApi gameApi;
    private final PaymentApi paymentApi;
    private final JwtUtil jwtUtil;
    private final RedisService redisService;

    public boolean requestOrder(GameIdListDto gameIdList, HttpServletRequest req) {
        // game-service 요청
        List<GameDto> gameDtoList = gameApi.subFind(gameIdList.getGameIdList());

        // Redis의 multiGet을 이용하여 GameID에 대한 재고를 Map으로 저장
        Map<String, Integer> eventStockMap = redisService.getMultiEventStock(gameDtoList);

        // 주문하려는 총 가격 저장 
        int totalPrice = getTotalPrice(gameDtoList, eventStockMap);
        
        // 주문 테이블에 저장
        Order order = orderRepository.save(Order.toEntity(totalPrice, getMemberId(req)));
        
        // 주문&게임 테이블에 저장
        List<OrderGame> orderGameList = createOrderGameList(gameDtoList, order);
        orderGameRepository.saveAll(orderGameList);
        
        // 할인 재고가 있는 게임 ID 리스트 추출
        List<Long> eventGameList = createEventGameList(gameDtoList, eventStockMap);
        
        // 게임 ID 리스트를 Redis에서 재고 1 감소
        redisService.bulkStockDecrease(eventGameList);
        // 비동기 통신으로 DB 재고 1 감소
        stockDecrease(eventGameList);
        
        // payment-service 요청
        PaymentResponseDto paymentResponseDto = processPayment(order);
        // 결제 결과에 따른 핸들링
        return handlingPaymentResult(paymentResponseDto, order, eventGameList);
    }

    public List<OrderResponseDto> list(int pageNo, int size, HttpServletRequest req) {
        // 주문번호 내림차순으로 Pagination
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by(Sort.Direction.DESC, "orderId"));

        // memberId를 기준으로 주문 테이블 검색
        Page<Order> page = orderRepository.findAllByMemberId(getMemberId(req), pageable);
        List<Order> orderList = page.getContent();

        // 주문에 해당하는 OrderGame 엔티티 조회
        List<OrderGame> orderGameList = orderGameRepository.findAllByOrderIdIn(
                orderList.stream().map(Order::getOrderId).toList()
        );

        // 외부 API로부터 게임 정보를 가져오기
        List<GameDto> gameDtoList = gameApi.subFind(
                orderGameList.stream().map(OrderGame::getGameId).toList()
        );

        // 주문별로 게임 리스트를 매핑하여 DTO 변환
        return orderList.stream()
                .map(order -> convertToDto(order, orderGameList, gameDtoList))
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean refund(Long orderId, HttpServletRequest req) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if(order != null && order.getMemberId().equals(getMemberId(req)) && Duration.between(order.getCreateAt(), LocalDateTime.now()).toHours() < 24 ){
            order.setOrderStatus(OrderStatus.REFUND);
            return true;
        }
        return false;
    }

    private boolean handlingPaymentResult(PaymentResponseDto paymentResponseDto, Order order, List<Long> eventGameList) {
        if(paymentResponseDto.isSuccess()){
            order.setOrderStatus(OrderStatus.PURCHASE);
            orderRepository.save(order);
            return true;
        } else {
            order.setOrderStatus(OrderStatus.CANCEL);
            orderRepository.save(order);
            redisService.bulkStockIncrease(eventGameList);
            stockIncrease(eventGameList);
            return false;
        }
    }

    private PaymentResponseDto processPayment(Order order) {
        PaymentRequestDto paymentRequestDto = PaymentRequestDto.builder()
                .totalPrice(order.getTotalPrice())
                .paymentWay(PaymentWay.CARD_PAYMENT)
                .orderId(order.getOrderId())
                .build();
        return paymentApi.payment(paymentRequestDto);
    }

    private List<Long> createEventGameList(List<GameDto> gameDtoList, Map<String, Integer> eventStockMap) {
        return gameDtoList.stream()
                .filter(gameDto -> {
                    Integer eventStock = eventStockMap.get("eventStock" + gameDto.getGameId());
                    return eventStock != null && eventStock > 0;
                })
                .map(GameDto::getGameId)
                .toList();
    }

    private List<OrderGame> createOrderGameList(List<GameDto> gameDtoList, Order order) {
        return gameDtoList.stream()
                .map(gameDto -> OrderGame.builder()
                        .price(gameDto.getPrice())
                        .gameId(gameDto.getGameId())
                        .orderId(order.getOrderId())
                        .build()
                )
                .collect(Collectors.toList());
    }

    private int getTotalPrice(List<GameDto> gameDtoList, Map<String, Integer> eventStockMap) {
        return gameDtoList.stream()
                .peek(gameDto -> {
                    Integer eventStock = eventStockMap.get("eventStock" + gameDto.getGameId());
                    if (eventStock != null && eventStock > 0) {
                        gameDto.setPrice((int) (gameDto.getPrice() * 0.8));
                    }
                })
                .mapToInt(GameDto::getPrice)
                .sum();
    }

    @Async
    protected void stockDecrease(List<Long> evnetGameList){
        gameApi.stockDecrease(evnetGameList);
    }

    @Async
    protected void stockIncrease(List<Long> evnetGameList){
        gameApi.stockIncrease(evnetGameList);
    }

    private OrderResponseDto convertToDto(Order order, List<OrderGame> orderGameList, List<GameDto> gameDtoList) {
        // 주문에 해당하는 게임 리스트 필터링
        List<GameDto> gameList = orderGameList.stream()
                .filter(orderGame -> orderGame.getOrderId().equals(order.getOrderId()))
                .map(orderGame -> gameDtoList.stream()
                        .filter(gameDto -> gameDto.getGameId().equals(orderGame.getGameId()))
                        .findFirst().orElse(null))
                .collect(Collectors.toList());

        return OrderResponseDto.builder()
                .orderId(order.getOrderId())
                .orderNumber(order.getOrderNumber())
                .totalPrice(order.getTotalPrice())
                .orderStatus(order.getOrderStatus())
                .gameList(gameList)
                .build();
    }

    @Scheduled(cron = "0 0/5 * * * *")
    @Transactional
    protected void updateOrderStatus(){
        List<Order> allList = orderRepository.findAllByOrderStatus(OrderStatus.PURCHASE);
        allList.stream()
                .filter(order -> Duration.between(order.getCreateAt(), LocalDateTime.now()).toHours() >= 24)
                .forEach(order -> order.setOrderStatus(OrderStatus.CONFIRM));
    }

    private Long getMemberId(HttpServletRequest req){
        String header = req.getHeader("Authorization");
        String token = jwtUtil.substringToken(header);
        Claims claims = jwtUtil.getTokenBody(token);
        return Long.parseLong(claims.get("memberId").toString());
    }
}
