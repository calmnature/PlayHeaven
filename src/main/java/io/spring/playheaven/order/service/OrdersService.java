package io.spring.playheaven.order.service;

import io.spring.playheaven.order.dto.OrdersRequestDto;
import io.spring.playheaven.order.entity.Orders;
import io.spring.playheaven.order.repository.OrdersRepository;
import io.spring.playheaven.ordergame.entity.OrdersGame;
import io.spring.playheaven.ordergame.repository.OrdersGameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrdersService {
    private final OrdersRepository orderRepository;
    private final OrdersGameRepository ordersGameRepository;

    public boolean purchase(OrdersRequestDto ordersRequestDto) {
        Orders orders = orderRepository.save(Orders.toEntity(ordersRequestDto));
        List<OrdersGame> ordersGameList = ordersRequestDto.getGameList().stream()
                .map(game -> new OrdersGame(orders, game))
                .collect(Collectors.toList());
        ordersGameRepository.saveAll(ordersGameList);
        return true;
    }
}
