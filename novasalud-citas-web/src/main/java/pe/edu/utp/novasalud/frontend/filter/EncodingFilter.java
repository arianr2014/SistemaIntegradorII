package pe.edu.utp.novasalud.frontend.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Fuerza UTF-8 en peticiones y respuestas.
 * Protege la interfaz contra textos con tildes mal interpretadas por Tomcat.
 */
public class EncodingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        /*
         * Solo forzamos Content-Type HTML en vistas dinámicas.
         * Los recursos estáticos como CSS o JS conservan su propio tipo MIME.
         */
        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            String uri = ((HttpServletRequest) request).getRequestURI().toLowerCase();
            if (!esRecursoEstatico(uri)) {
                ((HttpServletResponse) response).setContentType("text/html; charset=UTF-8");
            }
        }

        chain.doFilter(request, response);
    }

    /**
     * Identifica archivos estáticos para no alterar su Content-Type.
     */
    private boolean esRecursoEstatico(String uri) {
        return uri.endsWith(".css")
                || uri.endsWith(".js")
                || uri.endsWith(".png")
                || uri.endsWith(".jpg")
                || uri.endsWith(".jpeg")
                || uri.endsWith(".gif")
                || uri.endsWith(".svg")
                || uri.endsWith(".ico")
                || uri.endsWith(".webp");
    }
}
