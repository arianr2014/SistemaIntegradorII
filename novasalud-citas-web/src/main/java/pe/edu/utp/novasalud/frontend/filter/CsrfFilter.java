package pe.edu.utp.novasalud.frontend.filter;

import pe.edu.utp.novasalud.frontend.util.CsrfUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filtro CSRF para operaciones POST.
 *
 * V3.2:
 * - Todo formulario POST interno debe enviar el parámetro _csrf.
 * - Se excluye /login para permitir crear sesión antes de validar token.
 */
@WebFilter("/*")
public class CsrfFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String metodo = httpRequest.getMethod();
        String contextPath = httpRequest.getContextPath();
        String ruta = httpRequest.getRequestURI().substring(contextPath.length());

        if (!"POST".equalsIgnoreCase(metodo) || esRutaExcluida(ruta)) {
            chain.doFilter(request, response);
            return;
        }

        if (!CsrfUtil.tokenValido(httpRequest)) {
            httpResponse.sendRedirect(contextPath + "/acceso-denegado?from=csrf");
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean esRutaExcluida(String ruta) {
        return ruta.equals("/login")
                || ruta.startsWith("/resources/")
                || ruta.startsWith("/css/")
                || ruta.startsWith("/js/");
    }
}
