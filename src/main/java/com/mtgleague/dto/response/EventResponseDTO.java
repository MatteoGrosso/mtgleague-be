package com.mtgleague.dto.response;

import com.mtgleague.model.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EventResponseDTO {

    private Long id;
    private String name;
    private Date date;
    private int cap;
    private String description;
    private boolean started;
    private Set<Player> players;
}
