package com.mtgleague.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RoundRequestDTO {

    private Long roundId;
    private int p1Wins;
    private int p2Wins;
}
