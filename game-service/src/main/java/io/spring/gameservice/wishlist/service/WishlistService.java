package io.spring.gameservice.wishlist.service;

import io.spring.gameservice.game.entity.Game;
import io.spring.gameservice.game.repository.GameRepository;
import io.spring.gameservice.member.entity.Member;
import io.spring.gameservice.wishlist.dto.GameInfo;
import io.spring.gameservice.wishlist.dto.WishlistResponseDto;
import io.spring.gameservice.wishlist.entity.Wishlist;
import io.spring.gameservice.wishlist.entity.WishlistGame;
import io.spring.gameservice.wishlist.repository.WishlistGameRepository;
import io.spring.gameservice.wishlist.repository.WishlistRepository;
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
    private final GameRepository gameRepository;

    @Transactional
    public boolean regist(Long memberId, Long gameId) {
        // 위시리스트에 memberId가 있는 지 조회
        Wishlist wishlist = wishlistRepository.findByMember_MemberId(memberId).orElse(null);
        if(wishlist == null){
            wishlist = wishlistRepository.save(new Wishlist(Member.builder().memberId(memberId).build()));
        }

        Game game = gameRepository.findById(gameId).orElse(null);
        if(game == null || !game.isSaled()) return false;

        wishlist.setTotalPrice(wishlist.getTotalPrice() + game.getPrice());

        WishlistGame wishlistGame = WishlistGame.builder()
                .price(game.getPrice())
                .wishlist(wishlist)
                .game(game)
                .build();

        wishlistGameRepository.save(wishlistGame);
        return true;

    }

    public WishlistResponseDto list(Long memberId, int pageNo, int size) {
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by(Sort.Direction.DESC, "wishlistGameId"));
        Page<WishlistGame> page = wishlistGameRepository.findAll(pageable);
        List<WishlistGame> wishlistGameList = page.getContent();

        List<GameInfo> gameList = wishlistGameList.stream()
                                        .map(wishlistGame ->
                                            new GameInfo(wishlistGame.getGame().getGameId(),
                                                    wishlistGame.getGame().getGameName(),
                                                    wishlistGame.getGame().getPrice()))
                                        .toList();
        Wishlist wishlist = wishlistRepository.findByMember_MemberId(memberId).orElse(null);

        if(wishlist == null) return null;

        return new WishlistResponseDto(wishlist.getWishlistId(), wishlist.getTotalPrice(), gameList);
    }

    @Transactional
    public boolean delete(Long memberId, Long gameId) {
        Wishlist wishlist = wishlistRepository.findByMember_MemberId(memberId).orElse(null);
        if(wishlist != null){
            WishlistGame wishlistGame = wishlistGameRepository.findByWishlist_WishlistIdAndGame_GameId(wishlist.getWishlistId(), gameId).orElse(null);
            if(wishlistGame != null){
                wishlistGameRepository.delete(wishlistGame);
                log.warn("이전 총 가격 = {}", wishlist.getTotalPrice());
                wishlist.setTotalPrice(wishlist.getTotalPrice() - wishlistGame.getPrice());
                log.warn("이후 총 가격 = {}", wishlist.getTotalPrice());
                return true;
            }
        }
        return false;
    }
}
