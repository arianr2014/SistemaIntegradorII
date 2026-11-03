package pe.edu.utp.farmacia.frontend.util;

public final class JwtTokenContext {

    private static final ThreadLocal<String> TOKEN_HOLDER = new ThreadLocal<>();

    private JwtTokenContext() {
    }

    public static void setToken(String token) {
        TOKEN_HOLDER.set(token);
    }

    public static String getToken() {
        return TOKEN_HOLDER.get();
    }

    public static void clear() {
        TOKEN_HOLDER.remove();
    }
}
