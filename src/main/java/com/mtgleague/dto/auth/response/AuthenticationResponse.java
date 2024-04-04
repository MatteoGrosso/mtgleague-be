package com.mtgleague.dto.auth.response;

import com.mtgleague.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private Long userId;
    private String token;
    private Long expiresIn;
    private Role role;
}
