package pe.edu.utp.farmacia.frontend.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import pe.edu.utp.farmacia.frontend.model.UsuarioFarmacia;
import pe.edu.utp.farmacia.frontend.util.ApiClient;
import pe.edu.utp.farmacia.frontend.util.AppLogger;
import pe.edu.utp.farmacia.frontend.util.JwtTokenContext;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/usuarios")
public class UsuarioController extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String token = (session != null) ? (String) session.getAttribute("jwtToken") : null;
        JwtTokenContext.setToken(token);

        try {
            ApiClient.ApiResponse apiResp = ApiClient.get("/usuarios", token);
            if (apiResp.getStatusCode() == 200) {
                Type listType = new TypeToken<List<UsuarioFarmacia>>() {}.getType();
                List<UsuarioFarmacia> usuarios = gson.fromJson(apiResp.getBody(), listType);
                request.setAttribute("usuarios", usuarios);
            }
        } catch (Exception e) {
            AppLogger.error("Error obteniendo usuarios: " + e.getMessage(), e);
            request.setAttribute("error", "Error de conexión con la API.");
        }

        request.getRequestDispatcher("/WEB-INF/views/usuario/list.jsp").forward(request, response);
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
                Map<String, Object> data = buildUsuarioMap(request);
                data.put("password", request.getParameter("password"));
                ApiClient.post("/usuarios", gson.toJson(data), token);
            } else if ("actualizar".equals(action)) {
                int id = Integer.parseInt(request.getParameter("idUsuario"));
                Map<String, Object> data = buildUsuarioMap(request);
                data.put("idUsuario", id);
                String pwd = request.getParameter("password");
                if (pwd != null && !pwd.trim().isEmpty()) {
                    data.put("password", pwd.trim());
                }
                ApiClient.put("/usuarios/" + id, gson.toJson(data), token);
            } else if ("eliminar".equals(action)) {
                int id = Integer.parseInt(request.getParameter("idUsuario"));
                ApiClient.delete("/usuarios/" + id, token);
            }
        } catch (Exception e) {
            AppLogger.error("Error procesando usuario action=" + action + ": " + e.getMessage(), e);
        }

        response.sendRedirect(request.getContextPath() + "/usuarios");
    }

    private Map<String, Object> buildUsuarioMap(HttpServletRequest request) {
        Map<String, Object> data = new HashMap<>();
        data.put("username", request.getParameter("username"));
        data.put("nombres", request.getParameter("nombres"));
        data.put("apellidos", request.getParameter("apellidos"));
        data.put("email", request.getParameter("email"));
        data.put("rol", request.getParameter("rol"));
        data.put("estado", Boolean.parseBoolean(request.getParameter("estado")));
        return data;
    }
}
