package io.spring.playheaven.game.repository;

import io.spring.playheaven.game.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    Game findByGameName(String gameName);
}
