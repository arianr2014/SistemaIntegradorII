package pe.edu.utp.novasalud.backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import pe.edu.utp.novasalud.backend.model.Usuario;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Properties;

/**
 * Utilitario para la generación, firma y validación de tokens JWT (HMAC-SHA256).
 */
public final class JwtUtil {

    private static final String DEFAULT_SECRET = "NovaSaludSystemSecretKeyHMAC256BitsLongForSecureJwtTokenSigning2026UTP";
    private static final long EXPIRATION_TIME_MS = 8 * 60 * 60 * 1000L; // 8 horas

    private static final Key SIGNING_KEY;

    static {
        String secret = loadSecret();
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            byte[] padded = new byte[32];
            System.arraycopy(keyBytes, 0, padded, 0, Math.min(keyBytes.length, 32));
            keyBytes = padded;
        }
        SIGNING_KEY = Keys.hmacShaKeyFor(keyBytes);
    }

    private JwtUtil() {
    }

    /**
     * Genera un token JWT firmado digitalmente para el usuario autenticado.
     */
    public static String generarToken(Usuario usuario) {
        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + EXPIRATION_TIME_MS);

        return Jwts.builder()
                .setSubject(usuario.getUsername())
                .claim("idUsuario", usuario.getIdUsuario())
                .claim("rol", usuario.getRol())
                .claim("idMedico", usuario.getIdMedico())
                .claim("sessionVersion", usuario.getSessionVersion())
                .setIssuedAt(ahora)
                .setExpiration(expiracion)
                .signWith(SIGNING_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Valida la firma y expiración del token JWT.
     */
    public static Claims validarToken(String token) {
        if (token == null || token.isBlank()) return null;
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SIGNING_KEY)
                    .build()
                    .parseClaimsJws(token.trim())
                    .getBody();
        } catch (Exception ex) {
            AppLogger.warning(JwtUtil.class, "Token JWT inválido o expirado: " + ex.getMessage(), ex);
            return null;
        }
    }

    private static String loadSecret() {
        String envSecret = System.getenv("NOVASALUD_JWT_SECRET");
        if (envSecret != null && !envSecret.isBlank()) return envSecret.trim();

        try (InputStream input = JwtUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input != null) {
                Properties props = new Properties();
                props.load(input);
                String val = props.getProperty("jwt.secret");
                if (val != null && !val.isBlank()) return val.trim();
            }
        } catch (Exception ignored) {}

        return DEFAULT_SECRET;
    }
}
