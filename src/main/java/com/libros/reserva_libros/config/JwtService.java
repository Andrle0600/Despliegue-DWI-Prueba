package com.libros.reserva_libros.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {

    // Clave secreta
    private static final String SECRET_KEY = "b8Vz3Uqmt9x2kPZ4S4vF7n3wHr6LfVXcwUIWxJ9hv3E="; 

    // Genera un token
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // relacion del token al usuario
                .setIssuedAt(new Date()) // tiempo de generacion
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // duracion
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    //da el nombre del usuario con el token 
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    //validacion del token
    public boolean isTokenValid(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    //da la validacion del claim al token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //tiempo de expiracion del token
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    //fecha de expiracion del token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    //la clave cambia a uan version JWT
    private Key getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
    return Keys.hmacShaKeyFor(keyBytes);
    }
}
