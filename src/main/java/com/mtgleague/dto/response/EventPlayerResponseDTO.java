package com.mtgleague.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EventPlayerResponseDTO {

    private String name;
    private String surname;
    private int score;
    private double omw;
    private double gw;
    private double ogw;
}
