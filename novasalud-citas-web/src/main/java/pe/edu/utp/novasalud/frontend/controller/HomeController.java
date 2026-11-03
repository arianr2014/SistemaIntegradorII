package pe.edu.utp.novasalud.frontend.controller;

import pe.edu.utp.novasalud.frontend.util.ApiClient;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/inicio")
public class HomeController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> data = ApiClient.get("/reportes/dashboard", Map.class);

            if (data != null) {
                request.setAttribute("citasPorEstadoHoy", data.get("citasPorEstadoHoy"));
                request.setAttribute("medicosTurnoHoy", data.get("medicosTurnoHoy"));
                request.setAttribute("topEspecialidades", data.get("topEspecialidades"));
                request.setAttribute("resumenSemanal", data.get("resumenSemanal"));
                request.setAttribute("fechaHoy", data.get("fechaHoy"));
            }
        } catch (Exception ex) {
            throw new ServletException("No se pudo cargar el dashboard de inicio vía REST API", ex);
        }

        request.getRequestDispatcher("/WEB-INF/views/home/inicio.jsp").forward(request, response);
    }
}
