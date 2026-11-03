package pe.edu.utp.farmacia.frontend.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import pe.edu.utp.farmacia.frontend.model.Inventario;
import pe.edu.utp.farmacia.frontend.model.Medicamento;
import pe.edu.utp.farmacia.frontend.util.ApiClient;
import pe.edu.utp.farmacia.frontend.util.AppLogger;
import pe.edu.utp.farmacia.frontend.util.JwtTokenContext;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/inventario")
public class InventarioController extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String token = (session != null) ? (String) session.getAttribute("jwtToken") : null;
        JwtTokenContext.setToken(token);

        try {
            ApiClient.ApiResponse invResp = ApiClient.get("/inventario", token);
            if (invResp.getStatusCode() == 200) {
                Type listType = new TypeToken<List<Inventario>>() {}.getType();
                List<Inventario> inventarios = gson.fromJson(invResp.getBody(), listType);
                request.setAttribute("inventarios", inventarios);
            }

            ApiClient.ApiResponse medResp = ApiClient.get("/medicamentos", token);
            if (medResp.getStatusCode() == 200) {
                Type listType = new TypeToken<List<Medicamento>>() {}.getType();
                List<Medicamento> medicamentos = gson.fromJson(medResp.getBody(), listType);
                request.setAttribute("medicamentos", medicamentos);
            }

        } catch (Exception e) {
            AppLogger.error("Error obteniendo inventario: " + e.getMessage(), e);
            request.setAttribute("error", "Error de conexión con la API.");
        }

        request.getRequestDispatcher("/WEB-INF/views/inventario/list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String token = (session != null) ? (String) session.getAttribute("jwtToken") : null;
        JwtTokenContext.setToken(token);

        String action = request.getParameter("action");

        try {
            if ("actualizar".equals(action)) {
                int id = Integer.parseInt(request.getParameter("idInventario"));
                Map<String, Object> data = new HashMap<>();
                data.put("idInventario", id);
                data.put("idMedicamento", Integer.parseInt(request.getParameter("idMedicamento")));
                data.put("stockActual", Integer.parseInt(request.getParameter("stockActual")));
                data.put("stockMinimo", Integer.parseInt(request.getParameter("stockMinimo")));
                data.put("stockMaximo", Integer.parseInt(request.getParameter("stockMaximo")));
                data.put("ubicacion", request.getParameter("ubicacion"));

                ApiClient.put("/inventario/" + id, gson.toJson(data), token);
            }
        } catch (Exception e) {
            AppLogger.error("Error procesando inventario action=" + action + ": " + e.getMessage(), e);
        }

        response.sendRedirect(request.getContextPath() + "/inventario");
    }
}
