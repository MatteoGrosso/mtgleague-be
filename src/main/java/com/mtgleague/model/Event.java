package com.mtgleague.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Date date;
    private int cap;
    private String description;

    public Event(String name, Date date, int cap, String description) {
        this.name = name;
        this.date = date;
        this.cap = cap;
        this.description = description;
    }

    @ManyToMany
    @JoinTable(
            name = "registration",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id"))
    private Set<Player> players = new HashSet<>();

    public Long getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public Date getDate(){
        return date;
    }
    public int getCap(){
        return cap;
    }
    public String getDescription(){
        return description;
    }
    public Set<Player> getPlayers(){
        return players;
    }
}
