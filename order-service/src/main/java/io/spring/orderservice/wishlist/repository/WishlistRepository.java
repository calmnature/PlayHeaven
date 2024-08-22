package io.spring.orderservice.wishlist.repository;

import io.spring.orderservice.wishlist.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    Wishlist findByMemberId(Long memberId);
}
