package io.spring.orderservice.redis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

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
    public Integer getEventStock(String key){
        ValueOperations<String, Integer> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    public void stockDecrease(String key) {
        Integer eventStock = getEventStock(key);
        if(eventStock != null && eventStock > 0)
            redisTemplate.opsForValue().decrement(key, 1);
    }

    public void stockIncrease(String key) {
        redisTemplate.opsForValue().increment(key, 1);
    }
}
