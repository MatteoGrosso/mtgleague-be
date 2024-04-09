package com.mtgleague.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RoundRequestDTO {

    private Long roundId;
    private Long playerId;
    private int p1Wins;
    private int p2Wins;
}
