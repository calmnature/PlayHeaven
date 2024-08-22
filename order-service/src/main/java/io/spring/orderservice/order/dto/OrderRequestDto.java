package io.spring.orderservice.order.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderRequestDto {
    private Long memberId;
    // TODO : Request DTO 생성
    private List<Long> gameIdList;
}
