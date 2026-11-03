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
import pe.edu.utp.farmacia.frontend.util.ApiClient;
import pe.edu.utp.farmacia.frontend.util.AppLogger;
import pe.edu.utp.farmacia.frontend.util.JwtTokenContext;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/categorias")
public class CategoriaController extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String token = (session != null) ? (String) session.getAttribute("jwtToken") : null;
        JwtTokenContext.setToken(token);

        try {
            ApiClient.ApiResponse apiResp = ApiClient.get("/categorias", token);
            if (apiResp.getStatusCode() == 200) {
                Type listType = new TypeToken<List<CategoriaMedicamento>>() {}.getType();
                List<CategoriaMedicamento> categorias = gson.fromJson(apiResp.getBody(), listType);
                request.setAttribute("categorias", categorias);
            } else {
                request.setAttribute("error", "Error al cargar categorías.");
            }
        } catch (Exception e) {
            AppLogger.error("Error obteniendo categorías: " + e.getMessage(), e);
            request.setAttribute("error", "Error de conexión con la API.");
        }

        request.getRequestDispatcher("/WEB-INF/views/categoria/list.jsp").forward(request, response);
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
                data.put("nombreCategoria", request.getParameter("nombreCategoria"));
                data.put("descripcion", request.getParameter("descripcion"));
                data.put("requiereReceta", Boolean.parseBoolean(request.getParameter("requiereReceta")));

                ApiClient.post("/categorias", gson.toJson(data), token);
            } else if ("actualizar".equals(action)) {
                int id = Integer.parseInt(request.getParameter("idCategoria"));
                Map<String, Object> data = new HashMap<>();
                data.put("idCategoria", id);
                data.put("nombreCategoria", request.getParameter("nombreCategoria"));
                data.put("descripcion", request.getParameter("descripcion"));
                data.put("requiereReceta", Boolean.parseBoolean(request.getParameter("requiereReceta")));

                ApiClient.put("/categorias/" + id, gson.toJson(data), token);
            } else if ("eliminar".equals(action)) {
                int id = Integer.parseInt(request.getParameter("idCategoria"));
                ApiClient.delete("/categorias/" + id, token);
            }
        } catch (Exception e) {
            AppLogger.error("Error procesando categoría action=" + action + ": " + e.getMessage(), e);
        }

        response.sendRedirect(request.getContextPath() + "/categorias");
    }
}
