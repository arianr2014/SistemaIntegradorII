package pe.edu.utp.farmacia.rest;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Redirige las rutas /swagger y /swagger-ui hacia /swagger/index.html.
 */
@WebServlet({"/swagger", "/swagger-ui"})
public class SwaggerRedirectServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/swagger/index.html");
    }
}
