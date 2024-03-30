package com.mtgleague.service;

import com.mtgleague.dto.request.EventRequestDTO;
import com.mtgleague.dto.response.EventResponseDTO;
import com.mtgleague.model.Event;
import com.mtgleague.model.Player;
import com.mtgleague.repo.EventsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventsService {

    private final EventsRepository eventsRepository;

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

    public Event findById(Long id){
        return eventsRepository.getReferenceById(id);
    }

    private Event toEntity(EventRequestDTO eventRequestDTO){
        return new Event(eventRequestDTO.getName(), eventRequestDTO.getDate(), eventRequestDTO.getCap(), eventRequestDTO.getDescription());
    }

    private EventResponseDTO toDto(Event entity){
        return new EventResponseDTO(entity.getId(), entity.getName(), entity.getDate(), entity.getCap(), entity.getDescription(), entity.getPlayers());
    }

    public Event registerPlayer(Long eventId, Player playerToSubscribe) {
        Event selectedEvent= findById(eventId);
        Set<Player> playersSubscribed= selectedEvent.getPlayers();
        playersSubscribed.add(playerToSubscribe);
        selectedEvent.setPlayers(playersSubscribed);

        return eventsRepository.save(selectedEvent);
    }

    public Event unsubscribePlayer(Long eventId, Player playerToUnsubscribe) {
        Event selectedEvent= findById(eventId);
        Set<Player> playersSubscribed= selectedEvent.getPlayers();

        Set<Player> filteredPlayers = playersSubscribed.stream()
                .filter(player -> player.getId() != playerToUnsubscribe.getId())
                .collect(Collectors.toSet());

        selectedEvent.setPlayers(filteredPlayers);

        return eventsRepository.save(selectedEvent);
    }
}
