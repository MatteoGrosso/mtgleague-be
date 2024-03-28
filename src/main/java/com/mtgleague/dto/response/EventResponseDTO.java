package com.mtgleague.dto.response;

import com.mtgleague.model.Player;

import java.util.Date;
import java.util.Set;

public class EventResponseDTO {

    private Long id;
    private String name;
    private Date date;
    private int cap;
    private String description;

    private Set<Player> players;

    public EventResponseDTO(){}

    public EventResponseDTO(Long id, String name, Date date, int cap, String description, Set<Player> players){
        this.id= id;
        this.name= name;
        this.date= date;
        this.cap= cap;
        this.description= description;
        this.players= players;
    }

    //getters
    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Date getDate() {
        return date;
    }
    public int getCap() {
        return cap;
    }
    public String getDescription() {
        return description;
    }
    public Set<Player> getPlayers() {
        return players;
    }

    //setters
    public void setName(String name) {
        this.name = name;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public void setCap(int cap) {
        this.cap = cap;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
