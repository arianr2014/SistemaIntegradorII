package pe.edu.utp.novasalud.frontend.controller;

import pe.edu.utp.novasalud.frontend.util.ApiClient;
import pe.edu.utp.novasalud.frontend.util.AppLogger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Map;

/** Reportes de ingresos para Frontend Web Java. */
@WebServlet("/reportes-financieros")
public class ReporteFinancieroController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String inicio = texto(request.getParameter("fechaInicio"));
        String fin = texto(request.getParameter("fechaFin"));
        if (inicio.isBlank()) inicio = LocalDate.now().withDayOfMonth(1).toString();
        if (fin.isBlank()) fin = LocalDate.now().toString();

        try {
            String query = "/reportes/financiero?fechaInicio=" + URLEncoder.encode(inicio, StandardCharsets.UTF_8)
                    + "&fechaFin=" + URLEncoder.encode(fin, StandardCharsets.UTF_8);

            @SuppressWarnings("unchecked")
            Map<String, Object> data = ApiClient.get(query, Map.class);

            request.setAttribute("fechaInicio", inicio);
            request.setAttribute("fechaFin", fin);
            if (data != null) {
                request.setAttribute("totalIngresos", data.get("totalIngresos"));
                request.setAttribute("porEspecialidad", data.get("porEspecialidad"));
                request.setAttribute("porMetodo", data.get("porMetodo"));
            }
            request.getRequestDispatcher("/WEB-INF/views/reporte/financiero.jsp").forward(request, response);
        } catch (Exception ex) {
            AppLogger.error(getClass(), "Error cargando reportes financieros vía REST API", ex);
            throw new ServletException("No se pudieron cargar los reportes financieros.");
        }
    }

    private String texto(String v) { return v == null ? "" : v.trim(); }
}
