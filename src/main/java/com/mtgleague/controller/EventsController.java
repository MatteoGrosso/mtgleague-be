package com.mtgleague.controller;

import com.mtgleague.dto.request.EventRequestDTO;
import com.mtgleague.dto.response.EventResponseDTO;
import com.mtgleague.dto.response.GenericEntityListDTO;
import com.mtgleague.model.Event;
import com.mtgleague.service.EventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/events")
public class EventsController {

    @Autowired
    private EventsService eventsService;

    @GetMapping()
    public ResponseEntity<GenericEntityListDTO<EventResponseDTO>> getEvents(){
        GenericEntityListDTO<EventResponseDTO> events= new GenericEntityListDTO(eventsService.findAll());
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<Event> createEvent(@RequestBody EventRequestDTO eventRequestDTO){
        Event newEvent= eventsService.addEvent(eventRequestDTO);
        return new ResponseEntity<>(newEvent, HttpStatus.CREATED);
    }
}
