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

    private Long idP1;
    private String nameP1;
    private String surnameP1;
    private Long idP2;
    private String nameP2;
    private String surnameP2;

    private boolean ended; //used to know if this is the current round or not

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    private int p1Wins;
    private int p2Wins;

    public boolean isBye(){
        return idP2 == null;
    }
}
