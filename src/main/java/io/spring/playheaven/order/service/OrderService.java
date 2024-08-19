package io.spring.playheaven.order.service;

import io.spring.playheaven.order.constant.OrderStatus;
import io.spring.playheaven.order.dto.GameInfo;
import io.spring.playheaven.order.dto.OrderRequestDto;
import io.spring.playheaven.order.dto.OrderResponseDto;
import io.spring.playheaven.order.entity.Order;
import io.spring.playheaven.order.repository.OrderRepository;
import io.spring.playheaven.ordergame.entity.OrderGame;
import io.spring.playheaven.ordergame.repository.OrderGameRepository;
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
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderGameRepository orderGameRepository;

    public boolean purchase(OrderRequestDto orderRequestDto) {
        Order order = orderRepository.save(Order.toEntity(orderRequestDto));
        List<OrderGame> orderGameList = orderRequestDto.getGameList().stream()
                .map(game -> new OrderGame(order, game))
                .collect(Collectors.toList());
        orderGameRepository.saveAll(orderGameList);
        return true;
    }

    public List<OrderResponseDto> allList(Long memberId, int pageNo, int size) {
        // 주문번호 내림차순으로 Pagination
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by(Sort.Direction.DESC, "orderId"));
        // memberId를 기준으로 주문 테이블 검색
        Page<Order> page = orderRepository.findAllByMember_MemberId(memberId, pageable);
        List<Order> orderList = page.getContent();

        List<OrderGame> orderGameList = orderGameRepository.findAllByOrderIn(orderList);

        return convertToDtoList(orderGameList);
    }

    @Scheduled(cron = "* 0/5 * * * *")
    @Transactional
    protected void updateOrderStatus(){
        List<Order> allList = orderRepository.findAllByOrderStatus(OrderStatus.PURCHASE);
        allList.stream()
                .filter(order -> Duration.between(order.getCreateAt(), LocalDateTime.now()).toHours() >= 24)
                .forEach(order -> order.setOrderStatus(OrderStatus.CONFIRM));
    }

    private List<OrderResponseDto> convertToDtoList(List<OrderGame> orderGameList){
        // OrdersId를 기준으로 그룹화
        Map<Long, List<OrderGame>> groupedByOrderId = orderGameList.stream()
                                            .collect(Collectors.groupingBy(orderGame -> orderGame.getOrder().getOrderId()));

        // 그룹화된 각 주문별로 OrdersGameResponseDto 생성
        return groupedByOrderId.values().stream()
                .map(orderGroup -> {
                    Order firstOrder = orderGroup.get(0).getOrder();

                    List<GameInfo> gameList = orderGroup.stream()
                            .map(orderGame -> new GameInfo(
                                    orderGame.getGame().getGameId(),
                                    orderGame.getGame().getGameName(),
                                    orderGame.getGame().getPrice()
                            ))
                            .collect(Collectors.toList());

                    return new OrderResponseDto(
                            firstOrder.getOrderId(),
                            firstOrder.getOrderNumber(),
                            firstOrder.getTotalPrice(),
                            firstOrder.getOrderStatus(),
                            gameList
                    );
                })
                .collect(Collectors.toList());
    }
}
