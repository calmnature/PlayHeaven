package io.spring.playheaven.wishlist.service;

import io.spring.playheaven.game.entity.Game;
import io.spring.playheaven.game.repository.GameRepository;
import io.spring.playheaven.member.entity.Member;
import io.spring.playheaven.wishlist.entity.Wishlist;
import io.spring.playheaven.wishlist.entity.WishlistGame;
import io.spring.playheaven.wishlist.repository.WishlistGameRepository;
import io.spring.playheaven.wishlist.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
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
}
