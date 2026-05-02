package com.vinay.fortexaBackend.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JWTService {

    @Value("${jwt.secret}")
    private String secretKey;

    public String generateToken(String name, Boolean remember){
        Map<String, Object> claims = new HashMap<>();

        long expiry;
        if(Boolean.TRUE.equals(remember)){
            expiry = 1000L * 60 * 60 * 24 * 15;
        }else{
            expiry = 1000 * 60 * 15;
        }

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(name)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiry))
                .and()
                .signWith(getKey())
                .compact();
    }

    private SecretKey getKey() {
        byte[]keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


}
