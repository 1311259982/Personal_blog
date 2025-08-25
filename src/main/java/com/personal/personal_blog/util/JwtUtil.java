package com.personal.personal_blog.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

public class JwtUtil {
    private static final SecretKey jwtKey = Jwts.SIG.HS256.key().build();
    private static final long jwtExpiration = 1000 * 60 * 60 * 24;//24小时
    public static String generateToken(Map<String,Object> claims){
        return Jwts.builder()
                .claims(claims)
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(jwtKey)
                .compact();
    }
    public static Claims parseToken(String token){
        return Jwts.parser()
                .verifyWith(jwtKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
