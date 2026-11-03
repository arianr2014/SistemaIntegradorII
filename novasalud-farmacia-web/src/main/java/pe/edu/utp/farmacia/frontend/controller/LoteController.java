package pe.edu.utp.farmacia.frontend.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import pe.edu.utp.farmacia.frontend.model.LoteMedicamento;
import pe.edu.utp.farmacia.frontend.model.Medicamento;
import pe.edu.utp.farmacia.frontend.model.Proveedor;
import pe.edu.utp.farmacia.frontend.util.ApiClient;
import pe.edu.utp.farmacia.frontend.util.AppLogger;
import pe.edu.utp.farmacia.frontend.util.JwtTokenContext;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/lotes")
public class LoteController extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String token = (session != null) ? (String) session.getAttribute("jwtToken") : null;
        JwtTokenContext.setToken(token);

        try {
            ApiClient.ApiResponse loteResp = ApiClient.get("/lotes", token);
            if (loteResp.getStatusCode() == 200) {
                Type listType = new TypeToken<List<LoteMedicamento>>() {}.getType();
                List<LoteMedicamento> lotes = gson.fromJson(loteResp.getBody(), listType);
                request.setAttribute("lotes", lotes);
            }

            ApiClient.ApiResponse medResp = ApiClient.get("/medicamentos", token);
            if (medResp.getStatusCode() == 200) {
                Type listType = new TypeToken<List<Medicamento>>() {}.getType();
                List<Medicamento> medicamentos = gson.fromJson(medResp.getBody(), listType);
                request.setAttribute("medicamentos", medicamentos);
            }

            ApiClient.ApiResponse provResp = ApiClient.get("/proveedores", token);
            if (provResp.getStatusCode() == 200) {
                Type listType = new TypeToken<List<Proveedor>>() {}.getType();
                List<Proveedor> proveedores = gson.fromJson(provResp.getBody(), listType);
                request.setAttribute("proveedores", proveedores);
            }

        } catch (Exception e) {
            AppLogger.error("Error obteniendo lotes: " + e.getMessage(), e);
            request.setAttribute("error", "Error de conexión con la API.");
        }

        request.getRequestDispatcher("/WEB-INF/views/lote/list.jsp").forward(request, response);
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
                Map<String, Object> data = buildLoteMap(request);
                ApiClient.post("/lotes", gson.toJson(data), token);
            } else if ("actualizar".equals(action)) {
                int id = Integer.parseInt(request.getParameter("idLote"));
                Map<String, Object> data = buildLoteMap(request);
                data.put("idLote", id);
                ApiClient.put("/lotes/" + id, gson.toJson(data), token);
            } else if ("eliminar".equals(action)) {
                int id = Integer.parseInt(request.getParameter("idLote"));
                ApiClient.delete("/lotes/" + id, token);
            }
        } catch (Exception e) {
            AppLogger.error("Error procesando lote action=" + action + ": " + e.getMessage(), e);
        }

        response.sendRedirect(request.getContextPath() + "/lotes");
    }

    private Map<String, Object> buildLoteMap(HttpServletRequest request) {
        Map<String, Object> data = new HashMap<>();
        data.put("idMedicamento", Integer.parseInt(request.getParameter("idMedicamento")));
        data.put("numeroLote", request.getParameter("numeroLote"));
        data.put("fechaFabricacion", request.getParameter("fechaFabricacion"));
        data.put("fechaVencimiento", request.getParameter("fechaVencimiento"));
        data.put("cantidadInicial", Integer.parseInt(request.getParameter("cantidadInicial")));
        data.put("cantidadActual", Integer.parseInt(request.getParameter("cantidadActual")));
        data.put("precioCompra", new BigDecimal(request.getParameter("precioCompra")));
        data.put("idProveedor", Integer.parseInt(request.getParameter("idProveedor")));
        data.put("estado", Boolean.parseBoolean(request.getParameter("estado")));
        return data;
    }
}
