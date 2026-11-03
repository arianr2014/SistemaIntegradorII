package pe.edu.utp.farmacia.frontend.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import pe.edu.utp.farmacia.frontend.model.Proveedor;
import pe.edu.utp.farmacia.frontend.util.ApiClient;
import pe.edu.utp.farmacia.frontend.util.AppLogger;
import pe.edu.utp.farmacia.frontend.util.JwtTokenContext;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/proveedores")
public class ProveedorController extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String token = (session != null) ? (String) session.getAttribute("jwtToken") : null;
        JwtTokenContext.setToken(token);

        try {
            ApiClient.ApiResponse apiResp = ApiClient.get("/proveedores", token);
            if (apiResp.getStatusCode() == 200) {
                Type listType = new TypeToken<List<Proveedor>>() {}.getType();
                List<Proveedor> proveedores = gson.fromJson(apiResp.getBody(), listType);
                request.setAttribute("proveedores", proveedores);
            }
        } catch (Exception e) {
            AppLogger.error("Error obteniendo proveedores: " + e.getMessage(), e);
            request.setAttribute("error", "Error de conexión con la API.");
        }

        request.getRequestDispatcher("/WEB-INF/views/proveedor/list.jsp").forward(request, response);
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
                Map<String, Object> data = buildProveedorMap(request);
                ApiClient.post("/proveedores", gson.toJson(data), token);
            } else if ("actualizar".equals(action)) {
                int id = Integer.parseInt(request.getParameter("idProveedor"));
                Map<String, Object> data = buildProveedorMap(request);
                data.put("idProveedor", id);
                ApiClient.put("/proveedores/" + id, gson.toJson(data), token);
            } else if ("eliminar".equals(action)) {
                int id = Integer.parseInt(request.getParameter("idProveedor"));
                ApiClient.delete("/proveedores/" + id, token);
            }
        } catch (Exception e) {
            AppLogger.error("Error procesando proveedor action=" + action + ": " + e.getMessage(), e);
        }

        response.sendRedirect(request.getContextPath() + "/proveedores");
    }

    private Map<String, Object> buildProveedorMap(HttpServletRequest request) {
        Map<String, Object> data = new HashMap<>();
        data.put("ruc", request.getParameter("ruc"));
        data.put("razonSocial", request.getParameter("razonSocial"));
        data.put("contacto", request.getParameter("contacto"));
        data.put("telefono", request.getParameter("telefono"));
        data.put("email", request.getParameter("email"));
        data.put("direccion", request.getParameter("direccion"));
        data.put("estado", Boolean.parseBoolean(request.getParameter("estado")));
        return data;
    }
}
