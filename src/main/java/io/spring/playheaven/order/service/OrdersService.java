package io.spring.playheaven.order.service;

import io.spring.playheaven.order.dto.GameInfo;
import io.spring.playheaven.order.dto.OrdersRequestDto;
import io.spring.playheaven.order.entity.Orders;
import io.spring.playheaven.order.repository.OrdersRepository;
import io.spring.playheaven.order.dto.OrdersResponseDto;
import io.spring.playheaven.ordersgame.entity.OrdersGame;
import io.spring.playheaven.ordersgame.repository.OrdersGameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrdersService {
    private final OrdersRepository ordersRepository;
    private final OrdersGameRepository ordersGameRepository;

    public boolean purchase(OrdersRequestDto ordersRequestDto) {
        Orders orders = ordersRepository.save(Orders.toEntity(ordersRequestDto));
        List<OrdersGame> ordersGameList = ordersRequestDto.getGameList().stream()
                .map(game -> new OrdersGame(orders, game))
                .collect(Collectors.toList());
        ordersGameRepository.saveAll(ordersGameList);
        return true;
    }

    public List<OrdersResponseDto> allList(Long memberId, int pageNo, int size) {
        // 주문번호 내림차순으로 Pagination
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by(Sort.Direction.DESC, "ordersId"));
        // memberId를 기준으로 주문 테이블 검색
        Page<Orders> page = ordersRepository.findAllByMember_MemberId(memberId, pageable);
        List<Orders> ordersList = page.getContent();

        List<OrdersGame> ordersGameList = ordersGameRepository.findAllByOrdersIn(ordersList);

        return convertToDtoList(ordersGameList);
    }

    private List<OrdersResponseDto> convertToDtoList(List<OrdersGame> ordersGameList){
        // OrdersId를 기준으로 그룹화
        Map<Long, List<OrdersGame>> groupedByOrderId = ordersGameList.stream()
                                            .collect(Collectors.groupingBy(ordersGame -> ordersGame.getOrders().getOrdersId()));

        // 그룹화된 각 주문별로 OrdersGameResponseDto 생성
        return groupedByOrderId.values().stream()
                .map(ordersGroup -> {
                    Orders firstOrder = ordersGroup.get(0).getOrders();

                    List<GameInfo> gameList = ordersGroup.stream()
                            .map(ordersGame -> new GameInfo(
                                    ordersGame.getGame().getGameId(),
                                    ordersGame.getGame().getGameName(),
                                    ordersGame.getGame().getPrice()
                            ))
                            .collect(Collectors.toList());

                    return new OrdersResponseDto(
                            firstOrder.getOrdersId(),
                            firstOrder.getOrderNumber(),
                            firstOrder.getTotalPrice(),
                            gameList
                    );
                })
                .collect(Collectors.toList());
    }
}
