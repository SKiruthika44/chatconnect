package com.kiruthika.chatapp.gateway;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service

public class JwtService {

    private final String secretKey="0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef";



    public Claims validate(String token){
        return Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload();
    }
    private SecretKey getKey(){

        byte[] keyBytes= Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
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

    public String extractUsername(String token) {
        return validate(token).getSubject();
    }
}
