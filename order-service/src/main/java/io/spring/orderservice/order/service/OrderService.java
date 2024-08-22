package io.spring.orderservice.order.service;

import io.spring.orderservice.order.constant.OrderStatus;
import io.spring.orderservice.order.dto.GameDto;
import io.spring.orderservice.order.dto.OrderRequestDto;
import io.spring.orderservice.order.dto.OrderResponseDto;
import io.spring.orderservice.order.entity.Order;
import io.spring.orderservice.order.entity.OrderGame;
import io.spring.orderservice.order.feign.GameApi;
import io.spring.orderservice.order.repository.OrderGameRepository;
import io.spring.orderservice.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderGameRepository orderGameRepository;
    private final GameApi gameApi;

    public boolean purchase(OrderRequestDto orderRequestDto) {
        List<GameDto> gameDtoList = gameApi.subFind(orderRequestDto.getGameIdList());
        int totalPrice = gameDtoList.stream()
                .mapToInt(GameDto::getPrice)
                .sum();
        Order order = orderRepository.save(Order.toEntity(totalPrice, orderRequestDto.getMemberId()));

        List<OrderGame> orderGameList = gameDtoList.stream()
                .map(gameDto -> OrderGame.builder()
                        .price(gameDto.getPrice())
                        .gameId(gameDto.getGameId())
                        .orderId(order.getOrderId())
                        .build()
                )
                .collect(Collectors.toList());
        orderGameRepository.saveAll(orderGameList);
        return true;
    }

    public List<OrderResponseDto> allList(Long memberId, int pageNo, int size) {
        // 주문번호 내림차순으로 Pagination
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by(Sort.Direction.DESC, "orderId"));

        // memberId를 기준으로 주문 테이블 검색
        Page<Order> page = orderRepository.findAllByMemberId(memberId, pageable);
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

    @Transactional
    public boolean refund(Long memberId, Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if(order != null && order.getMemberId().equals(memberId)){
            if(Duration.between(order.getCreateAt(), LocalDateTime.now()).toHours() <= 24 && order.getOrderStatus().equals(OrderStatus.PURCHASE)){
                order.setOrderStatus(OrderStatus.REFUND);
                return true;
            }
        }
        return false;
    }

    @Scheduled(cron = "0 0/5 * * * *")
    @Transactional
    protected void updateOrderStatus(){
        List<Order> allList = orderRepository.findAllByOrderStatus(OrderStatus.PURCHASE);
        allList.stream()
                .filter(order -> Duration.between(order.getCreateAt(), LocalDateTime.now()).toHours() >= 24)
                .forEach(order -> order.setOrderStatus(OrderStatus.CONFIRM));
    }
}
