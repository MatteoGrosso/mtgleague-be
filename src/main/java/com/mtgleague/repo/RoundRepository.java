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

    @Query("SELECT r FROM Round r WHERE r.idP1 = :playerId OR r.idP2 = :playerId")
    List<Round> findByPlayerId(@Param("playerId") Long playerId);

    @Query("SELECT r FROM Round r WHERE (r.idP1 = :playerId OR r.idP2 = :playerId) AND r.event.id = :eventId")
    List<Round> findByPlayerIdAndEventId(@Param("playerId") Long playerId, @Param("eventId") Long eventId);

    @Query("SELECT r FROM Round r WHERE (r.idP1 = :playerId OR r.idP2 = :playerId) ORDER BY r.turn DESC LIMIT 1")
    Optional<Round> findCurrentByPlayerId(@Param("playerId") Long playerId);

    @Query("SELECT r FROM Round r WHERE r.event.id = :eventId AND not ended")
    List<Round> findCurrentByEventId(@Param("eventId") Long eventId);
}