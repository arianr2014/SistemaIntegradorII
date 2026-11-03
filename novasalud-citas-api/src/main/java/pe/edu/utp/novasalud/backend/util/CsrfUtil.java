package pe.edu.utp.novasalud.backend.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utilidad CSRF.
 *
 * V3.2:
 * Protege formularios POST críticos mediante un token guardado en sesión.
 */
public final class CsrfUtil {

    public static final String SESSION_TOKEN = "CSRF_TOKEN";
    public static final String PARAM_TOKEN = "_csrf";
    private static final SecureRandom RANDOM = new SecureRandom();

    private CsrfUtil() {
    }

    public static String obtenerToken(HttpSession session) {
        if (session == null) {
            return "";
        }
        Object actual = session.getAttribute(SESSION_TOKEN);
        if (actual != null) {
            return String.valueOf(actual);
        }
        String nuevo = generarToken();
        session.setAttribute(SESSION_TOKEN, nuevo);
        return nuevo;
    }

    public static boolean tokenValido(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        Object tokenSesion = session.getAttribute(SESSION_TOKEN);
        String tokenRequest = request.getParameter(PARAM_TOKEN);
        return tokenSesion != null && tokenRequest != null && tokenSesion.equals(tokenRequest);
    }

    private static String generarToken() {
        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
