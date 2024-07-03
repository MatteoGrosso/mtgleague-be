package com.mtgleague.repo;


import com.mtgleague.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventsRepository extends JpaRepository<Event, Long> {

    @Query("SELECT r FROM Event r ORDER by date ASC")
    List<Event> findAllOrdered();
}