package com.mtgleague.service;

import com.mtgleague.dto.request.EventRequestDTO;
import com.mtgleague.dto.response.EventResponseDTO;
import com.mtgleague.model.Event;
import com.mtgleague.repo.EventsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventsService {
    @Autowired
    private EventsRepository eventsRepository;

    public List<EventResponseDTO> findAll(){
        List<Event> results= eventsRepository.findAll();
        List<EventResponseDTO> dtos= new ArrayList<>();
        results.forEach(event -> {
            dtos.add(toDto(event));
        });
        return dtos;
    }

    public Event addEvent(EventRequestDTO event){
        return eventsRepository.save(toEntity(event));
    }

    private Event toEntity(EventRequestDTO eventRequestDTO){
        return new Event(eventRequestDTO.getName(), eventRequestDTO.getDate(), eventRequestDTO.getCap(), eventRequestDTO.getDescription());
    }

    private EventResponseDTO toDto(Event entity){
        return new EventResponseDTO(entity.getId(), entity.getName(), entity.getDate(), entity.getCap(), entity.getDescription(), entity.getPlayers());
    }
}
