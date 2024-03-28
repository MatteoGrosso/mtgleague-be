package com.mtgleague.dto.response;

import com.mtgleague.model.Event;

import java.util.Set;

public class PlayerResponseDTO {

    private Long id;
    private String name;
    private String surname;
    private String email;
    private Set<Event> events;
    private int score;
    private int eventsPlayed;
    private Double winRate;

    public PlayerResponseDTO(){}

    public PlayerResponseDTO(Long id, String name, String surname, String email, Set<Event> events, int score, int eventsPlayed, Double winRate){
        this.id= id;
        this.name= name;
        this.surname= surname;
        this.email= email;
        this.events= events;
        this.score= score;
        this.eventsPlayed= eventsPlayed;
        this.winRate= winRate;
    }

    //getters
    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getSurname() {
        return surname;
    }
    public String getEmail() {
        return email;
    }
    public Set<Event> getEvents() {
        return events;
    }
    public int getScore() {
        return score;
    }
    public int getEventsPlayed() {
        return eventsPlayed;
    }
    public Double getWinRate() {
        return winRate;
    }

    //setters
    public void setName(String name) {
        this.name = name;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setEvents(Set<Event> events) {
        this.events = events;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public void setEventsPlayed(int eventsPlayed) {
        this.eventsPlayed = eventsPlayed;
    }
    public void setWinRate(Double winRate) {
        this.winRate = winRate;
    }
}
