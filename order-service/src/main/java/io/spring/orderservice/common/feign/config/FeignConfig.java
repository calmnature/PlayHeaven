package io.spring.orderservice.common.feign.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignConfig {
    // Request Header에 있는 JWT 토큰을 추출하여
    // Feign Client에게 전달하기 위해 RequestInterceptor 사용
    @Bean
    public RequestInterceptor requestInterceptor(){
        return requestTemplate -> {
            HttpServletRequest req = getCurrentHttpRequest();
            String token = getJwtToken(req);
            if(token != null)
                requestTemplate.header("Authorization", token);
        };
    }

    // 웹 요청이 있을 때 마다 현재의 요청을 가져오기 위해
    // RequestContextHolder를 사용하여 현재 스레드의 HttpServletReqeust를 가지고 옴
    private HttpServletRequest getCurrentHttpRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }

    private String getJwtToken(HttpServletRequest req) {
        return req.getHeader("Authorization");
    }
}
