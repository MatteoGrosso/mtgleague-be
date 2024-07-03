package com.mtgleague.repo;


import com.mtgleague.model.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RankingRepository extends JpaRepository<Ranking, Long> {

    @Query("SELECT r FROM Ranking r WHERE r.eventId = :eventId")
    List<Ranking> findByEventId(@Param("eventId") Long eventId);

    @Query("SELECT r FROM Ranking r WHERE r.isValid = true AND r.eventId is null")
    List<Ranking> getLatestGeneralRanking();

    @Query("UPDATE Ranking r SET r.isValid = false WHERE r.isValid = true")
    @Modifying
    @Transactional
    void updateIsValidField();

}