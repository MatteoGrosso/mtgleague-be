package com.mtgleague.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UtilityAuthDTO {
    private String token;
    private Long expirationDate;
}
