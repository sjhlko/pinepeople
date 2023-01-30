package com.lion.pinepeople.utils;

import com.lion.pinepeople.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Slf4j
public class JwtTokenUtil {

    /**
     * 토큰이 유효한지 체크하는 메서드
     *
     * @param request 요청
     * @param token   토큰
     * @param key     키
     * @return boolean 유효하면 true, 유효하지 않으면 false
     */
    public static boolean isValidToken(HttpServletRequest request, String token, String key) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            request.setAttribute("exception", ErrorCode.EXPIRE_TOKEN);
        } catch (RuntimeException e) {
            // 토큰 만료를 제외한 나머지 예외 처리
        }
        return false;
    }

    /**
     * UserId를 반환하는 메서드
     *
     * @param token 토큰
     * @param key   키
     * @return Long userId
     */
    public static Long getUserId(String token, String key) {
        return Long.valueOf(Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().get("userId").toString());
    }

    /**
     * 토큰 생성 메서드
     *
     * @param userId       claims에 담을 userId
     * @param key          키
     * @param expireTimeMs 만료시간
     * @return String 토큰
     */
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
