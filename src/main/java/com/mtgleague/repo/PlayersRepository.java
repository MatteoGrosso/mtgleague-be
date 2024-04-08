package com.mtgleague.repo;

import com.mtgleague.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayersRepository extends JpaRepository<Player, Long> {
    Optional<Player> findByEmail(String email);

    @Query("SELECT p FROM Player p WHERE p.id = :playerId")
    Optional<Player> getByIdString(@Param("playerId")String playerId);
}