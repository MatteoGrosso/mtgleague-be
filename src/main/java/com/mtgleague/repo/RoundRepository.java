package com.mtgleague.repo;

import com.mtgleague.model.Round;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoundRepository extends JpaRepository<Round, Long> {

    @Query("SELECT r FROM Round r WHERE (idp1 = :playerId or idp2 = :playerId) AND started = true")
    Optional<List<Round>> findByPlayerId(@Param("playerId") Long playerId);
}