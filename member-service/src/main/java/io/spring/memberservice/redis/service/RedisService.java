package io.spring.memberservice.redis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${jwt.token.refresh-expiration}")
    private long refreshTokenTime;

    // K : 이메일, V : 인증 코드
    public void setCode(String email, String authCode){
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        // 유효 시간은 300, 단위는 초로 설정
        valueOperations.set(email, authCode, 300, TimeUnit.SECONDS);
    }
    
    // K(email)의 V(인증 코드)를 반환
    public String getCode(String email) throws AuthenticationException {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String authCode = valueOperations.get(email);
        if(authCode == null)
            throw new AuthenticationException("인증 코드가 일치하지 않습니다.");
        return authCode;
    }

    public void saveRefreshToken(String subject, String refreshToken) {
        redisTemplate.opsForValue().set(
                        subject,
                        refreshToken,
                        refreshTokenTime,
                        TimeUnit.MILLISECONDS
        );
    }

    public Boolean deleteRefreshToken(String subject) {
        return redisTemplate.delete(subject);
    }
}
