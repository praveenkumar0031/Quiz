package dev.com.quiz.Utils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;


import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {

    private static final String SECRET =
            "quizz-app-devloped-by-praveen-kumar-S"; // ≥ 32 chars

    private static final SecretKey key =
            Keys.hmacShaKeyFor(SECRET.getBytes());

    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour

    // Generate Token
    public static String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    // Extract username
    public static String extractUsername(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    // Validate Token
    public static boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parse(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}

