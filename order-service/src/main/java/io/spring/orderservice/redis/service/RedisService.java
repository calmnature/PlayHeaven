package io.spring.orderservice.redis.service;

import io.spring.orderservice.common.dto.GameDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Integer> redisTemplate;

    @Value("${spring.data.redis.event-stock.expiration}")
    private long eventStockTTL;

    public void setEventStock(String key, int eventStock) {
        redisTemplate.opsForValue().set(
                key,
                eventStock,
                eventStockTTL,
                TimeUnit.MILLISECONDS
        );
    }

    public Map<String, Integer> getMultiEventStock(List<GameDto> gameDtoList) {
        List<String> keys = gameDtoList.stream()
                .map(gameDto -> "eventStock" + gameDto.getGameId())
                .toList();

        List<Integer> values = redisTemplate.opsForValue().multiGet(keys);

        Map<String, Integer> eventStockMap = new HashMap<>();
        int size = keys.size();
        for(int i = 0; i < size; i++){
            String key = keys.get(i);
            Integer value = values.get(i);
            if(value == null){
                int eventStock = gameDtoList.get(i).getEventStock();
                if(eventStock == 0) continue;
                setEventStock(key, eventStock);
                value = eventStock;
            }
            eventStockMap.put(key, value);
        }

        return eventStockMap;
    }

    public void bulkStockDecrease(List<Long> eventGameList) {
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for(Long gameId : eventGameList){
                String key = "eventStock" + gameId;
                connection.stringCommands().decr(key.getBytes());
            }
            return null;
        });
    }

    public void bulkStockIncrease(List<Long> eventGameList) {
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for(Long gameId : eventGameList){
                String key = "eventStock" + gameId;
                connection.stringCommands().incr(key.getBytes());
            }
            return null;
        });
    }
}
