package com.mtgleague.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    private boolean started;
    private Date date;
    private int cap;
    private String description;

    private int currentTurn;

    private int maxTurn;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "registration",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id"))
    private Set<Player> players = new HashSet<>();

    @OneToMany(mappedBy="event")
    private Set<Round> matches = new HashSet<>();

    public Event(String name, Date date, int cap, String description) {
        this.name = name;
        this.date = date;
        this.cap = cap;
        this.description = description;
    }

}
