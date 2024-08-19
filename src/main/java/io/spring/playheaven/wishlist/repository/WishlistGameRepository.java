package io.spring.playheaven.wishlist.repository;

import io.spring.playheaven.wishlist.entity.Wishlist;
import io.spring.playheaven.wishlist.entity.WishlistGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistGameRepository extends JpaRepository<WishlistGame, Long> {
}
