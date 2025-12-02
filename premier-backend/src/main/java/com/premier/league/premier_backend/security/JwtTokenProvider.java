package com.premier.league.premier_backend.security;

import com.premier.league.premier_backend.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    public String generarAccessToken(String nombreUsuario, Long idUsuario) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getExpiration());

        return Jwts.builder()
                .subject(nombreUsuario)
                .claim("idUsuario", idUsuario)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    public String generarRefreshToken(String nombreUsuario, Long idUsuario) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getRefreshExpiration());

        return Jwts.builder()
                .subject(nombreUsuario)
                .claim("idUsuario", idUsuario)
                .claim("tipo", "REFRESH")
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    public String obtenerNombreUsuarioDelToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    public Long obtenerIdUsuarioDelToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get("idUsuario", Long.class);
    }

    public boolean validarToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
