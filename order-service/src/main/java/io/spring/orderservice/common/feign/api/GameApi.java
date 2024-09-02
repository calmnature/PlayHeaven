package io.spring.orderservice.common.feign.api;

import io.spring.orderservice.common.dto.GameDto;
import io.spring.orderservice.common.feign.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

// FeignClient에 인증 헤더를 추가하기 위해
// 생성한 FeignConfig.class를 설정에 추가
@FeignClient(name = "GameApi", url = "http://localhost:9999/game-service/v1/", configuration = FeignConfig.class)
public interface GameApi {
    @PostMapping("/subFind")
    List<GameDto> subFind(@RequestBody List<Long> gameIdList);

    @GetMapping("/findById/{gameId}")
    GameDto findById(@PathVariable Long gameId);
}
