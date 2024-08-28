package io.spring.orderservice.wishlist.service;

import io.jsonwebtoken.Claims;
import io.spring.orderservice.common.dto.GameDto;
import io.spring.orderservice.common.jwt.JwtUtil;
import io.spring.orderservice.wishlist.dto.WishlistResponseDto;
import io.spring.orderservice.wishlist.entity.Wishlist;
import io.spring.orderservice.wishlist.entity.WishlistGame;
import io.spring.orderservice.common.feign.api.GameApi;
import io.spring.orderservice.wishlist.repository.WishlistGameRepository;
import io.spring.orderservice.wishlist.repository.WishlistRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final WishlistGameRepository wishlistGameRepository;
    private final GameApi gameApi;
    private final JwtUtil jwtUtil;

    @Transactional
    public boolean regist(Long gameId, HttpServletRequest req) {
        Long memberId = getMemberId(req);
        // 위시리스트에 memberId가 있는 지 조회
        Wishlist wishlist = wishlistRepository.findByMemberId(memberId);
        if(wishlist == null){
            wishlist = wishlistRepository.save(Wishlist.builder().memberId(memberId).build());
        }

        GameDto game = gameApi.findById(gameId);
        Optional<WishlistGame> optionalWishlistGame = wishlistGameRepository.findByWishlistIdAndGameId(wishlist.getWishlistId(), game.getGameId());
        if(game == null || !game.isSaled() || optionalWishlistGame.isPresent()) return false;

        wishlist.setTotalPrice(wishlist.getTotalPrice() + game.getPrice());

        wishlistGameRepository.save(WishlistGame.builder()
                .price(game.getPrice())
                .wishlistId(wishlist.getWishlistId())
                .gameId(gameId)
                .build()
        );

        return true;
    }

    public WishlistResponseDto list(int pageNo, int size, HttpServletRequest req) {
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by(Sort.Direction.DESC, "wishlistGameId"));

        Page<WishlistGame> page = wishlistGameRepository.findAll(pageable);

        List<WishlistGame> wishlistGameList = page.getContent();

        List<GameDto> gameList = gameApi.subFind(
                wishlistGameList.stream().map(WishlistGame::getGameId).toList()
        );

        Wishlist wishlist = wishlistRepository.findByMemberId(getMemberId(req));

        if(wishlist == null) return null;

        return new WishlistResponseDto(wishlist.getWishlistId(), wishlist.getTotalPrice(), gameList);
    }

    @Transactional
    public boolean delete(Long gameId, HttpServletRequest req) {
        Wishlist wishlist = wishlistRepository.findByMemberId(getMemberId(req));

        if(wishlist != null){
            Optional<WishlistGame> optionalWishlistGame = wishlistGameRepository.findByWishlistIdAndGameId(wishlist.getWishlistId(), gameId);

            if(optionalWishlistGame.isPresent()){
                WishlistGame wishlistGame = optionalWishlistGame.get();
                wishlistGameRepository.delete(wishlistGame);
                wishlist.setTotalPrice(wishlist.getTotalPrice() - wishlistGame.getPrice());
                return true;
            }
        }
        return false;
    }

    private Long getMemberId(HttpServletRequest req){
        String header = req.getHeader("Authorization");
        String token = jwtUtil.substringToken(header);
        Claims claims = jwtUtil.getTokenBody(token);
        return Long.parseLong(claims.get("memberId").toString());
    }
}
