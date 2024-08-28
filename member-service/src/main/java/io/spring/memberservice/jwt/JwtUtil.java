package io.spring.memberservice.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.spring.memberservice.member.entity.Member;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    public static final String AUTHORIZATION_HEADER = "Authorization"; // Header KEY 값
    public static final String AUTHORIZATION_KEY = "auth"; // 사용자 권한 값의 KEY
    public static final String BEARER_PREFIX = "Bearer "; // Token 식별자
    @Value("${jwt.token.access-expiration}")
    private long ACCESS_TOKEN_TIME; // 액세스 토큰 만료시간(ms)
    @Value("${jwt.token.refresh-expiration}")
    private long REFRESH_TOKEN_TIME; // 리프레시 토큰 만료시간(ms)

    // Base64 Encode 한 SecretKey
    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // 액세스 토큰 생성
    public String createAccessToken(Member member) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(member.getEmail()) // 사용자 Email
                        .claim(AUTHORIZATION_KEY, member.getRole()) // 사용자 권한
                        .claim("memberId", member.getMemberId()) // Member PK
                        .claim("name", member.getName()) // 사용자 이름
                        .claim("nickname", member.getNickname()) // 사용자 닉네임
                        .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }

    // 리프레쉬 토큰 생성
    public String createRefreshToken(Member member) {
        Date date = new Date();

        return Jwts.builder()
                    .setSubject(member.getEmail()) // 사용자 Email
                    .setIssuedAt(date) // 발급일
                    .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME)) // 만료 시간
                    .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                    .compact();
    }

    // JWT Cookie 에 저장
    public void addJwtToHeader(String token, HttpServletResponse res) {
        // JWT 헤더에 추가
        res.setHeader(AUTHORIZATION_HEADER, token);
    }

    // JWT 토큰 substring
    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        throw new NullPointerException("Not Found Token");
    }

    public String getTokenBody(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }
}