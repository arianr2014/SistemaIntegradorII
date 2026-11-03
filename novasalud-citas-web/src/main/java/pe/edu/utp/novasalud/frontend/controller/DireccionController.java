package pe.edu.utp.novasalud.frontend.controller;

import pe.edu.utp.novasalud.frontend.util.ApiClient;
import pe.edu.utp.novasalud.frontend.util.AppLogger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/** Dashboard ejecutivo para Dirección en Frontend Web Java. */
@WebServlet("/direccion")
public class DireccionController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> data = ApiClient.get("/reportes/direccion", Map.class);

            if (data != null) {
                request.setAttribute("fechaHoy", data.get("fechaHoy"));
                request.setAttribute("citasHoy", data.get("citasHoy"));
                request.setAttribute("ingresosMes", data.get("ingresosMes"));
                request.setAttribute("topEspecialidades", data.get("topEspecialidades"));
                request.setAttribute("ingresosEspecialidad", data.get("ingresosEspecialidad"));
            }
            request.getRequestDispatcher("/WEB-INF/views/direccion/index.jsp").forward(request, response);
        } catch (Exception ex) {
            AppLogger.error(getClass(), "Error cargando dirección vía REST API", ex);
            throw new ServletException("No se pudo cargar el dashboard de dirección.");
        }
    }
}
