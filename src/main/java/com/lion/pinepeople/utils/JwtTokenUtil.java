package com.lion.pinepeople.utils;

import com.lion.pinepeople.exception.ErrorCode;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Slf4j
public class JwtTokenUtil {

    private static Claims extractClaims(String token, String key){
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }

    public static String isValid(String token, String key){
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
            return "OK";
        } catch (ExpiredJwtException e) {
            return ErrorCode.EXPIRE_TOKEN.name();
        } catch (Exception e) {
            return ErrorCode.INVALID_TOKEN.name();
        }
    }

    public static Long getUserId(String token, String key) {
        return Long.valueOf(extractClaims(token, key).get("userId").toString());
    }

    public static String createToken(Long userId, String key, long expireTimeMs) {
        Claims claims = Jwts.claims();
        claims.put("userId", userId);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }
}
