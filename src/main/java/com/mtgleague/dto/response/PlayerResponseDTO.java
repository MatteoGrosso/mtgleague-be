package com.mtgleague.dto.response;

import com.mtgleague.model.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PlayerResponseDTO {

    private Long id;
    private String name;
    private String surname;
    private String email;
    private Set<Event> events;
    private int score;
    private int winRate;
}
