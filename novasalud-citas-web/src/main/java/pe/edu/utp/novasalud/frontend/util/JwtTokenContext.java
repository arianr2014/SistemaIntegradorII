package pe.edu.utp.novasalud.frontend.util;

/**
 * Contexto ThreadLocal para almacenar y proveer el token JWT de la sesión HTTP actual
 * hacia ApiClient.java en todas las peticiones REST de los Servlets Controllers.
 */
public final class JwtTokenContext {

    private static final ThreadLocal<String> CURRENT_TOKEN = new ThreadLocal<>();

    private JwtTokenContext() {
    }

    public static void setToken(String token) {
        if (token != null && !token.isBlank()) {
            CURRENT_TOKEN.set(token.trim());
        } else {
            CURRENT_TOKEN.remove();
        }
    }

    public static String getToken() {
        return CURRENT_TOKEN.get();
    }

    public static void clear() {
        CURRENT_TOKEN.remove();
    }
}
