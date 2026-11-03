package pe.edu.utp.farmacia.frontend.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
import java.util.HashMap;
import java.util.Map;

@WebServlet({"/login", "/logout"})
public class AuthController extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();

        if ("/logout".equals(path)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            JwtTokenContext.clear();
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Check if already logged in
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuarioLogueado") != null) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            request.setAttribute("error", "Por favor ingrese usuario y contraseña.");
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
            return;
        }

        Map<String, String> loginData = new HashMap<>();
        loginData.put("username", username.trim());
        loginData.put("password", password.trim());

        try {
            ApiClient.ApiResponse apiResp = ApiClient.post("/auth/login", gson.toJson(loginData), null);

            if (apiResp.getStatusCode() == 200) {
                JsonObject jsonObject = gson.fromJson(apiResp.getBody(), JsonObject.class);
                String token = jsonObject.has("token") ? jsonObject.get("token").getAsString() : "";

                UsuarioFarmacia usuario;
                if (jsonObject.has("usuario") && !jsonObject.get("usuario").isJsonNull()) {
                    usuario = gson.fromJson(jsonObject.getAsJsonObject("usuario"), UsuarioFarmacia.class);
                } else {
                    usuario = gson.fromJson(jsonObject, UsuarioFarmacia.class);
                }

                HttpSession session = request.getSession(true);
                session.setAttribute("usuarioLogueado", usuario);
                session.setAttribute("jwtToken", token);

                JwtTokenContext.setToken(token);
                AppLogger.info("Usuario inició sesión exitosamente: " + usuario.getUsername());

                response.sendRedirect(request.getContextPath() + "/home");
            } else {
                String msg = "Credenciales inválidas.";
                try {
                    JsonObject jsonObject = gson.fromJson(apiResp.getBody(), JsonObject.class);
                    if (jsonObject != null) {
                        if (jsonObject.has("error")) {
                            msg = jsonObject.get("error").getAsString();
                        } else if (jsonObject.has("mensaje")) {
                            msg = jsonObject.get("mensaje").getAsString();
                        }
                    }
                } catch (Exception ignored) {
                }
                request.setAttribute("error", msg);
                request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            AppLogger.error("Error durante el inicio de sesión: " + e.getMessage(), e);
            String detailMsg = e.getMessage() != null ? e.getMessage() : "Error de conexión";
            request.setAttribute("error", "No se pudo conectar con el servidor backend API (" + detailMsg + ")");
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
        }
    }
}
