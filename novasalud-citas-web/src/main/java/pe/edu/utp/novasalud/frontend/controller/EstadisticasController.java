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

/** Estadísticas clínicas para Frontend Web Java. */
@WebServlet("/estadisticas")
public class EstadisticasController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String inicio = texto(request.getParameter("fechaInicio"));
        String fin = texto(request.getParameter("fechaFin"));
        if (inicio.isBlank()) inicio = LocalDate.now().minusDays(6).toString();
        if (fin.isBlank()) fin = LocalDate.now().toString();

        try {
            String query = "/reportes/estadisticas?fechaInicio=" + URLEncoder.encode(inicio, StandardCharsets.UTF_8)
                    + "&fechaFin=" + URLEncoder.encode(fin, StandardCharsets.UTF_8);

            @SuppressWarnings("unchecked")
            Map<String, Object> data = ApiClient.get(query, Map.class);

            request.setAttribute("fechaInicio", inicio);
            request.setAttribute("fechaFin", fin);
            if (data != null) {
                request.setAttribute("topEspecialidades", data.get("topEspecialidades"));
                request.setAttribute("resumenSemanal", data.get("resumenSemanal"));
            }
            request.getRequestDispatcher("/WEB-INF/views/estadistica/index.jsp").forward(request, response);
        } catch (Exception ex) {
            AppLogger.error(getClass(), "Error cargando estadísticas vía REST API", ex);
            throw new ServletException("No se pudieron cargar las estadísticas.");
        }
    }

    private String texto(String v) { return v == null ? "" : v.trim(); }
}
