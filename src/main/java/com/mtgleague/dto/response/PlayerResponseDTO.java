package com.mtgleague.dto.response;

import com.mtgleague.model.Event;
import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PlayerResponseDTO {

    private Long id;
    private String name;
    private String surname;
    private String email;
    private Set<Event> events;
    private int score;
    private int winRate;
    private String role;
}
