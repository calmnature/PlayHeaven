package io.spring.playheaven.wishlist.repository;

import io.spring.playheaven.wishlist.entity.WishlistGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WishlistGameRepository extends JpaRepository<WishlistGame, Long> {
    Optional<WishlistGame> findByWishlist_WishlistIdAndGame_GameId(Long wishlistId, Long gameId);
}
