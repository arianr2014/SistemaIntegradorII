package pe.edu.utp.farmacia.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public final class JwtUtil {

    private static final String SECRET_KEY_STRING = "NovaSaludFarmaciaSystemSecretKeyHMAC256BitsLongForSecureJwtTokenSigning2026UTP";
    private static final Key SIGNING_KEY = Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes());
    private static final long EXPIRATION_TIME = 86400000L; // 24 horas

    private JwtUtil() {
    }

    public static String generarToken(String username, String rol, int idUsuario) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("rol", rol);
        claims.put("idUsuario", idUsuario);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SIGNING_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public static Claims obtenerClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SIGNING_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static String obtenerUsername(String token) {
        return obtenerClaims(token).getSubject();
    }

    public static boolean esTokenValido(String token) {
        try {
            Claims claims = obtenerClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
