package com.mtgleague.controller;

import com.mtgleague.dto.request.EventRequestDTO;
import com.mtgleague.dto.request.PlayerRequestDTO;
import com.mtgleague.dto.response.EventResponseDTO;
import com.mtgleague.dto.response.GenericEntityListDTO;
import com.mtgleague.model.Event;
import com.mtgleague.model.Player;
import com.mtgleague.service.EventsService;
import com.mtgleague.service.PlayersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventsController {

    private final EventsService eventsService;
    private final PlayersService playersService;

    @GetMapping()
    public ResponseEntity<GenericEntityListDTO<EventResponseDTO>> getEvents(){
        GenericEntityListDTO<EventResponseDTO> events= new GenericEntityListDTO(eventsService.findAll());
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponseDTO> findEventById(@PathVariable("eventId") Long eventId){
        EventResponseDTO event= eventsService.findByIdDTO(eventId);
        return new ResponseEntity<>(event, HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<Event> createEvent(@RequestBody EventRequestDTO eventRequestDTO){
        Event newEvent= eventsService.addEvent(eventRequestDTO);
        return new ResponseEntity<>(newEvent, HttpStatus.CREATED);
    }

    @PostMapping("/{eventId}/register")
    public ResponseEntity<Event> registerPlayer(@PathVariable("eventId") Long eventId, @RequestBody PlayerRequestDTO request){
        Player playerToSubscribe= playersService.findById(request.getPlayerId());
        Event updatedEvent= eventsService.registerPlayer(eventId, playerToSubscribe);
        return new ResponseEntity<>(updatedEvent, HttpStatus.CREATED);
    }

    @PostMapping("/{eventId}/quit")
    public ResponseEntity<Event> unsubscribePlayer(@PathVariable("eventId") Long eventId, @RequestBody PlayerRequestDTO request){
        Player playerToUnsubscribe= playersService.findById(request.getPlayerId());
        Event updatedEvent= eventsService.unsubscribePlayer(eventId, playerToUnsubscribe);
        return new ResponseEntity<>(updatedEvent, HttpStatus.CREATED);
    }

    @PostMapping("/{eventId}/start")
    public ResponseEntity startEvent(@PathVariable("eventId") Long eventId) throws Exception {
        eventsService.calculatePairings(eventId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
