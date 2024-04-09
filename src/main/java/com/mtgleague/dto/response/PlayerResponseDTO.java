package com.mtgleague.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PlayerResponseDTO {

    private String name;
    private String surname;
    private String role;
    private double matchWinRate;
    private int score;
    private int eventsPlayed;
}
