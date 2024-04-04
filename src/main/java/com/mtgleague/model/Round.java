package com.mtgleague.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
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
