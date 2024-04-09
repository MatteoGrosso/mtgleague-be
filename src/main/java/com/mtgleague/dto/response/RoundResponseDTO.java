package com.mtgleague.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RoundResponseDTO {

    private Long id;

    private String nameP1;
    private String surnameP1;

    private String nameP2;
    private String surnameP2;

    private boolean ended;
    private boolean eventEnded;

    private int p1Wins;
    private int p2Wins;
    private boolean bye;

}
