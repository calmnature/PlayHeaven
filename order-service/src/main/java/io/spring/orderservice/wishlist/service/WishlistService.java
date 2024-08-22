package io.spring.orderservice.wishlist.service;

import io.spring.orderservice.wishlist.dto.GameDto;
import io.spring.orderservice.wishlist.dto.WishlistResponseDto;
import io.spring.orderservice.wishlist.entity.Wishlist;
import io.spring.orderservice.wishlist.entity.WishlistGame;
import io.spring.orderservice.wishlist.fegin.GameApi;
import io.spring.orderservice.wishlist.repository.WishlistGameRepository;
import io.spring.orderservice.wishlist.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final WishlistGameRepository wishlistGameRepository;
    private final GameApi gameApi;

    @Transactional
    public boolean regist(Long memberId, Long gameId) {
        // 위시리스트에 memberId가 있는 지 조회
        Wishlist wishlist = wishlistRepository.findByMemberId(memberId);
        if(wishlist == null){
            wishlist = wishlistRepository.save(Wishlist.builder().memberId(memberId).build());
        }

        GameDto game = gameApi.findById(gameId);
        if(game == null || !game.isSaled()) return false;

        wishlist.setTotalPrice(wishlist.getTotalPrice() + game.getPrice());

        WishlistGame wishlistGame = WishlistGame.builder()
                .price(game.getPrice())
                .wishlistId(wishlist.getWishlistId())
                .gameId(gameId)
                .build();

        wishlistGameRepository.save(wishlistGame);
        return true;

    }

    public WishlistResponseDto list(Long memberId, int pageNo, int size) {
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by(Sort.Direction.DESC, "wishlistGameId"));
        // TODO : 해당 Wishlist에 담긴 게임 정보를 List로 만들어서 WishlistResponseDto로 변환
        Page<WishlistGame> page = wishlistGameRepository.findAll(pageable);
        List<WishlistGame> wishlistGameList = page.getContent();

        List<GameDto> gameList = gameApi.subFind(
                wishlistGameList.stream().map(WishlistGame::getGameId).toList()
        );

        Wishlist wishlist = wishlistRepository.findByMemberId(memberId);

        if(wishlist == null) return null;

        // TODO : wishlist에 추가 필요
        return new WishlistResponseDto(wishlist.getWishlistId(), wishlist.getTotalPrice(), gameList);
    }

    @Transactional
    public boolean delete(Long memberId, Long gameId) {
        Wishlist wishlist = wishlistRepository.findByMemberId(memberId);
        // TODO : MemberId에 해당하는 위시리스트에 gameId가 등록되어있으면 삭제
        if(wishlist != null){
            WishlistGame wishlistGame = wishlistGameRepository.findByWishlistIdAndGameId(wishlist.getWishlistId(), gameId);
            if(wishlistGame != null){
                wishlistGameRepository.delete(wishlistGame);
                wishlist.setTotalPrice(wishlist.getTotalPrice() - wishlistGame.getPrice());
                return true;
            }
        }
        return false;
    }
}
