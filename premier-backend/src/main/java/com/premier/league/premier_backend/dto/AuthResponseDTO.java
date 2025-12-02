package com.premier.league.premier_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
    private String accessToken;
    private String refreshToken;
    private Long idUsuario;
    private String nombreUsuario;
    private String email;
}
