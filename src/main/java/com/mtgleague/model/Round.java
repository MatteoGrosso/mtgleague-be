package com.mtgleague.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Round {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean started= false;

    private Long idP1;
    private Long idP2;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    private int p1Wins;
    private int p2Wins;
}
