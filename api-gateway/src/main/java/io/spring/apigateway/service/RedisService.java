package io.spring.apigateway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;

   public String getRefreshToken(String subject){
       return redisTemplate.opsForValue().get(subject);
   }
}
