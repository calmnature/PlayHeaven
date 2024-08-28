// TODO 중간 테이블 필요없을 거 같음?

package io.spring.orderservice.wishlist.repository;

import io.spring.orderservice.wishlist.entity.WishlistGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WishlistGameRepository extends JpaRepository<WishlistGame, Long> {
    Optional<WishlistGame> findByWishlistIdAndGameId(Long wishlistId, Long gameId);
}
