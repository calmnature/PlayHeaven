package io.spring.orderservice.common.feign;

import io.spring.orderservice.common.dto.GameDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "GameApi", url = "http://localhost:8083/api/game")
public interface GameApi {
    @PostMapping("/subFind")
    List<GameDto> subFind(@RequestBody List<Long> gameIdList);

    @GetMapping("/findById/{gameId}")
    GameDto findById(@PathVariable Long gameId);
}
