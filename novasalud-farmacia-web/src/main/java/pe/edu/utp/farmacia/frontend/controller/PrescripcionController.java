package pe.edu.utp.farmacia.frontend.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import pe.edu.utp.farmacia.frontend.model.Prescripcion;
import pe.edu.utp.farmacia.frontend.util.ApiClient;
import pe.edu.utp.farmacia.frontend.util.AppLogger;
import pe.edu.utp.farmacia.frontend.util.JwtTokenContext;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/prescripciones")
public class PrescripcionController extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String token = (session != null) ? (String) session.getAttribute("jwtToken") : null;
        JwtTokenContext.setToken(token);

        try {
            ApiClient.ApiResponse apiResp = ApiClient.get("/prescripciones", token);
            if (apiResp.getStatusCode() == 200) {
                Type listType = new TypeToken<List<Prescripcion>>() {}.getType();
                List<Prescripcion> prescripciones = gson.fromJson(apiResp.getBody(), listType);
                request.setAttribute("prescripciones", prescripciones);
            }
        } catch (Exception e) {
            AppLogger.error("Error obteniendo prescripciones: " + e.getMessage(), e);
            request.setAttribute("error", "Error de conexión con la API.");
        }

        request.getRequestDispatcher("/WEB-INF/views/prescripcion/list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String token = (session != null) ? (String) session.getAttribute("jwtToken") : null;
        JwtTokenContext.setToken(token);

        String action = request.getParameter("action");

        try {
            if ("crear".equals(action)) {
                Map<String, Object> data = new HashMap<>();
                String codigo = "PRES-2026-" + String.format("%04d", (int)(Math.random() * 9000 + 1000));
                data.put("codigoPrescripcion", codigo);
                data.put("numeroColegiaturaMedico", request.getParameter("numeroColegiaturaMedico"));
                data.put("nombreMedico", request.getParameter("nombreMedico"));
                data.put("dniPaciente", request.getParameter("dniPaciente"));
                data.put("nombrePaciente", request.getParameter("nombrePaciente"));
                data.put("diagnostico", request.getParameter("diagnostico"));
                data.put("instruccionesGenerales", request.getParameter("diagnostico"));
                data.put("fechaEmision", request.getParameter("fechaEmision"));
                data.put("fechaVencimiento", request.getParameter("fechaVencimiento"));
                data.put("estado", "VIGENTE");

                ApiClient.post("/prescripciones", gson.toJson(data), token);
            }
        } catch (Exception e) {
            AppLogger.error("Error procesando prescripción action=" + action + ": " + e.getMessage(), e);
        }

        response.sendRedirect(request.getContextPath() + "/prescripciones");
    }
}
