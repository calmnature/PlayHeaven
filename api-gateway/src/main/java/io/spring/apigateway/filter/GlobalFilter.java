package io.spring.apigateway.filter;

import io.jsonwebtoken.*;
import io.spring.apigateway.jwt.JwtUtil;
import io.spring.apigateway.service.RedisService;
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
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j(topic = "API Gateway-GlobalFilter")
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {
    private final JwtUtil jwtUtil;
    private final RedisService redisService;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    public static class Config{}

    public GlobalFilter(JwtUtil jwtUtil, RedisService redisService){
        super(Config.class);
        this.jwtUtil = jwtUtil;
        this.redisService = redisService;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            HttpHeaders headers = request.getHeaders();
            String authorization = headers.getFirst("Authorization");
            List<String> whiteList = Arrays.asList(
              "/member-service/v1/email-overlap/**",
              "/member-service/v1/email-auth/**",
              "/member-service/v1/nickname-overlap/**",
              "/member-service/v1/regist",
              "/member-service/v1/login"
            );

            String path = request.getURI().getPath();
            log.info("Request Path = {}", path);

            boolean isPathAllowed = whiteList.stream()
                    .anyMatch(pattern -> antPathMatcher.match(pattern, path));

            if(isPathAllowed){
                log.info("Routing Whitelist");
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
                return chain.filter(exchange);
            } catch (SecurityException | MalformedJwtException | SignatureException e) {
                return onError(exchange, "유효하지 않는 JWT 서명 입니다.", HttpStatus.UNAUTHORIZED);
            } catch (ExpiredJwtException e) {
                log.info("Access Token 만료");
                Claims claims = e.getClaims();

                log.info("Redis에 저장된 RefreshToken 추출");
                String refreshToken = redisService.getRefreshToken(claims.getSubject());

                if(refreshToken != null && jwtUtil.validateToken(refreshToken)){
                    log.info("유효한 Refresh Token으로 Access Token 재발급");
                    String newAccessToken = jwtUtil.createAccessToken(claims);
                    jwtUtil.addJwtToHeader(newAccessToken, exchange.getResponse());
                    return chain.filter(exchange);
                } else{
                    log.info("유효하지 않은 Refresh Token(기간 만료 or Refresh Token 존재하지 않음)");
                    return onError(exchange, "로그인 후 이용 가능합니다.", HttpStatus.UNAUTHORIZED);
                }
            } catch (UnsupportedJwtException e) {
                return onError(exchange, "지원되지 않는 JWT 토큰 입니다.", HttpStatus.UNAUTHORIZED);
            } catch (IllegalArgumentException e) {
                return onError(exchange, "잘못된 JWT 토큰 입니다.", HttpStatus.UNAUTHORIZED);
            }
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
