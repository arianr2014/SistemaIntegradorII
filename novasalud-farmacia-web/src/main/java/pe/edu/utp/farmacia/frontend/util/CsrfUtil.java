package pe.edu.utp.farmacia.frontend.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.UUID;

public final class CsrfUtil {

    public static final String CSRF_SESSION_ATTR = "csrfToken";
    public static final String CSRF_PARAM_NAME = "csrfToken";

    private CsrfUtil() {
    }

    public static String getToken(HttpSession session) {
        if (session == null) {
            return null;
        }
        String token = (String) session.getAttribute(CSRF_SESSION_ATTR);
        if (token == null || token.isBlank()) {
            token = UUID.randomUUID().toString();
            session.setAttribute(CSRF_SESSION_ATTR, token);
        }
        return token;
    }

    public static boolean validateToken(HttpSession session, String requestToken) {
        if (session == null || requestToken == null || requestToken.isBlank()) {
            return false;
        }
        String sessionToken = (String) session.getAttribute(CSRF_SESSION_ATTR);
        return sessionToken != null && sessionToken.equals(requestToken.trim());
    }

    public static boolean isValidToken(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        String requestToken = request.getParameter(CSRF_PARAM_NAME);
        if (requestToken == null || requestToken.isBlank()) {
            requestToken = request.getHeader("X-CSRF-TOKEN");
        }
        return validateToken(session, requestToken);
    }
}
