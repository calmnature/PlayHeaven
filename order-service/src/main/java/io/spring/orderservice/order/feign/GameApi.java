package io.spring.orderservice.order.feign;

import io.spring.orderservice.order.dto.GameDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "GameApi", url = "http://localhost:8083/api/game")
public interface GameApi {
    @PostMapping("/subFind")
    List<GameDto> subFind(@RequestBody List<Long> gameIdList);
}
