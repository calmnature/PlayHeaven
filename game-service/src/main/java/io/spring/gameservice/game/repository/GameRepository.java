package io.spring.gameservice.game.repository;

import io.spring.gameservice.game.entity.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    Game findByGameName(String gameName);

    Page<Game> findAllBySaledIsTrue(Pageable pageable);

    Optional<Game> findByGameIdAndSaledIsTrue(Long gameId);
}


