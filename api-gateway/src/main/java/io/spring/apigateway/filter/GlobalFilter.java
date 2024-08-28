package io.spring.apigateway.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.spring.apigateway.jwt.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j(topic = "API Gateway-GlobalFilter")
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {
    private final JwtUtil jwtUtil;

    public static class Config{}

    public GlobalFilter(JwtUtil jwtUtil){
        super(Config.class);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            HttpHeaders headers = request.getHeaders();
            String authorization = headers.getFirst("Authorization");
            List<String> whiteList = Arrays.asList(
              "/member-service/api/member/regist",
              "/member-service/api/member/login"
            );

            String path = request.getURI().getPath();
            log.info("Request Path = {}", path);
            if(whiteList.contains(path)){
                log.info("Routing login or regist");
                return chain.filter(exchange);
            }

            if(authorization == null){
                log.info("UNAUTHORIZED, Required Login");
                return onError(exchange, "로그인 후 이용 가능합니다.", HttpStatus.UNAUTHORIZED);
            }

            String token = jwtUtil.substringToken(authorization);
            log.info("authorization = {}", authorization);

            try {
                jwtUtil.validateToken(token);
            } catch (SecurityException | MalformedJwtException | SignatureException e) {
                return onError(exchange, "유효하지 않는 JWT 서명 입니다.", HttpStatus.UNAUTHORIZED);
            } catch (ExpiredJwtException e) {
                return onError(exchange, "만료된 JWT 토큰 입니다.", HttpStatus.UNAUTHORIZED);
            } catch (UnsupportedJwtException e) {
                return onError(exchange, "지원되지 않는 JWT 토큰 입니다.", HttpStatus.UNAUTHORIZED);
            } catch (IllegalArgumentException e) {
                return onError(exchange, "잘못된 JWT 토큰 입니다.", HttpStatus.UNAUTHORIZED);
            }

            return chain.filter(exchange);
        });
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus){
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        log.info(err);
        byte[] bytes = err.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        response.getHeaders().setContentType(MediaType.TEXT_PLAIN);

        return response.writeWith(Mono.just(buffer)).doOnError(error -> DataBufferUtils.release(buffer));
    }
}
