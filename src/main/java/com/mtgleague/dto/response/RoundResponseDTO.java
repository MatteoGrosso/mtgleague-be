package com.mtgleague.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RoundResponseDTO {

    private Long id;

    private Long idP1;
    private String nameP1;
    private String surnameP1;

    private Long idP2;
    private String nameP2;
    private String surnameP2;

    public boolean ended;
}
