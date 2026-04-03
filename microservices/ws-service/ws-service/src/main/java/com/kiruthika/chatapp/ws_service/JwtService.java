package com.kiruthika.chatapp.ws_service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service

public class JwtService {

    private final String secretKey="your-secret-key-here";



    public Claims validate(String token){
        return Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload();
    }
    private SecretKey getKey(){

        byte[] keyBytes= Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {

        return validate(token).getSubject();
    }

    public boolean isTokenValid(String token) {

        try {
            validate(token);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}
