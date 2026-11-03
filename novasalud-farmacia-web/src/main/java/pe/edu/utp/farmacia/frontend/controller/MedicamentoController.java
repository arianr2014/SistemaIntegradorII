package pe.edu.utp.farmacia.frontend.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import pe.edu.utp.farmacia.frontend.model.CategoriaMedicamento;
import pe.edu.utp.farmacia.frontend.model.Medicamento;
import pe.edu.utp.farmacia.frontend.util.ApiClient;
import pe.edu.utp.farmacia.frontend.util.AppLogger;
import pe.edu.utp.farmacia.frontend.util.JwtTokenContext;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/medicamentos")
public class MedicamentoController extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String token = (session != null) ? (String) session.getAttribute("jwtToken") : null;
        JwtTokenContext.setToken(token);

        try {
            ApiClient.ApiResponse medResp = ApiClient.get("/medicamentos", token);
            if (medResp.getStatusCode() == 200) {
                Type listType = new TypeToken<List<Medicamento>>() {}.getType();
                List<Medicamento> medicamentos = gson.fromJson(medResp.getBody(), listType);
                request.setAttribute("medicamentos", medicamentos);
            }

            ApiClient.ApiResponse catResp = ApiClient.get("/categorias", token);
            if (catResp.getStatusCode() == 200) {
                Type listType = new TypeToken<List<CategoriaMedicamento>>() {}.getType();
                List<CategoriaMedicamento> categorias = gson.fromJson(catResp.getBody(), listType);
                request.setAttribute("categorias", categorias);
            }
        } catch (Exception e) {
            AppLogger.error("Error obteniendo medicamentos: " + e.getMessage(), e);
            request.setAttribute("error", "Error de conexión con la API.");
        }

        request.getRequestDispatcher("/WEB-INF/views/medicamento/list.jsp").forward(request, response);
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
                Map<String, Object> data = buildMedicamentoMap(request);
                ApiClient.post("/medicamentos", gson.toJson(data), token);
            } else if ("actualizar".equals(action)) {
                int id = Integer.parseInt(request.getParameter("idMedicamento"));
                Map<String, Object> data = buildMedicamentoMap(request);
                data.put("idMedicamento", id);
                ApiClient.put("/medicamentos/" + id, gson.toJson(data), token);
            } else if ("eliminar".equals(action)) {
                int id = Integer.parseInt(request.getParameter("idMedicamento"));
                ApiClient.delete("/medicamentos/" + id, token);
            }
        } catch (Exception e) {
            AppLogger.error("Error procesando medicamento action=" + action + ": " + e.getMessage(), e);
        }

        response.sendRedirect(request.getContextPath() + "/medicamentos");
    }

    private Map<String, Object> buildMedicamentoMap(HttpServletRequest request) {
        Map<String, Object> data = new HashMap<>();
        data.put("codigoBarras", request.getParameter("codigoBarras"));
        data.put("nombreComercial", request.getParameter("nombreComercial"));
        data.put("nombreGenerico", request.getParameter("nombreGenerico"));
        data.put("idCategoria", Integer.parseInt(request.getParameter("idCategoria")));
        data.put("presentacion", request.getParameter("presentacion"));
        data.put("concentracion", request.getParameter("concentracion"));
        data.put("precioVenta", new BigDecimal(request.getParameter("precioVenta")));
        data.put("requiereReceta", Boolean.parseBoolean(request.getParameter("requiereReceta")));
        data.put("estado", Boolean.parseBoolean(request.getParameter("estado")));
        return data;
    }
}
