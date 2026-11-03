package pe.edu.utp.novasalud.frontend.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import pe.edu.utp.novasalud.frontend.model.Usuario;
import pe.edu.utp.novasalud.frontend.util.ApiClient;
import pe.edu.utp.novasalud.frontend.util.CsrfUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador de autenticación para Frontend Web.
 * Invoca el servicio REST `/api/auth/login` en novasalud-backend-api y almacena el JWT en la sesión.
 */
@WebServlet({"/login", "/logout"})
public class AuthController extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String servletPath = request.getServletPath();

        if ("/logout".equals(servletPath)) {
            cerrarSesion(request, response);
            return;
        }

        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("usuarioLogeado") != null) {
            String rol = (String) session.getAttribute("rolUsuario");
            String rutaInicio = obtenerRutaInicioPorRol(rol);

            if ("/login".equals(rutaInicio)) {
                session.invalidate();
                response.sendRedirect(request.getContextPath() + "/login?error=rol");
                return;
            }

            response.sendRedirect(request.getContextPath() + rutaInicio);
            return;
        }

        request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String username = texto(request.getParameter("username"));
        String password = texto(request.getParameter("password"));

        if (username.isBlank() || password.isBlank()) {
            request.setAttribute("error", "Usuario y contraseña son obligatorios.");
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
            return;
        }

        try {
            Map<String, String> creds = new HashMap<>();
            creds.put("username", username);
            creds.put("password", password);

            String jsonResponse = ApiClient.post("/auth/login", creds, String.class);

            if (jsonResponse != null && !jsonResponse.isBlank()) {
                JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
                String jwtToken = jsonObject.has("token") ? jsonObject.get("token").getAsString() : null;
                JsonObject userObject = jsonObject.has("usuario") ? jsonObject.getAsJsonObject("usuario") : jsonObject;

                Usuario usuario = gson.fromJson(userObject, Usuario.class);

                if (usuario != null) {
                    if (usuario.esDoctor() && !usuario.tieneMedicoAsociado()) {
                        request.setAttribute("error", "Su usuario médico no está vinculado a un médico activo. Contacte al administrador.");
                        request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
                        return;
                    }

                    iniciarSesionSegura(request, response, usuario, jwtToken);
                    return;
                }
            }

        } catch (Exception ex) {
            System.err.println("[AUTH_CONTROLLER_ERROR] Error al autenticar usuario '" + username + "': " + ex.getMessage());
            ex.printStackTrace();
            String msg = ex.getMessage();
            if (msg == null || msg.isBlank()) msg = ex.getClass().getSimpleName();
            request.setAttribute("error", "Usuario o contraseña incorrectos o error de servicio (" + msg + ").");
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
            return;
        }

        request.setAttribute("error", "Usuario o contraseña incorrectos.");
        request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
    }

    private void iniciarSesionSegura(HttpServletRequest request, HttpServletResponse response, Usuario usuario, String jwtToken)
            throws IOException {

        HttpSession sesionAnterior = request.getSession(false);
        if (sesionAnterior != null) {
            sesionAnterior.invalidate();
        }

        HttpSession session = request.getSession(true);
        session.setMaxInactiveInterval(30 * 60);

        session.setAttribute("usuarioLogeado", usuario.getUsername());
        session.setAttribute("rolUsuario", usuario.getRol());
        session.setAttribute("idUsuario", usuario.getIdUsuario());
        session.setAttribute("sessionVersion", usuario.getSessionVersion());
        session.setAttribute("jwtToken", jwtToken);

        session.setAttribute("idMedico", usuario.getIdMedico());
        session.setAttribute("nombreMedico", usuario.getMedicoNombreCompleto());
        session.setAttribute("idEspecialidad", usuario.getIdEspecialidad());
        session.setAttribute("nombreEspecialidad", usuario.getEspecialidadNombre());

        CsrfUtil.obtenerToken(session);

        response.sendRedirect(request.getContextPath() + obtenerRutaInicioPorRol(usuario.getRol()));
    }

    private void cerrarSesion(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        response.sendRedirect(request.getContextPath() + "/login?logout=1");
    }

    private String obtenerRutaInicioPorRol(String rol) {
        String rolNormalizado = rol == null ? "" : rol.trim().toUpperCase();

        switch (rolNormalizado) {
            case "ADMIN":
                return "/inicio";
            case "RECEPCIONISTA":
                return "/citas";
            case "DOCTOR":
                return "/agenda-medico";
            case "CAJERO":
                return "/pagos";
            case "DIRECCION":
                return "/direccion";
            default:
                return "/login";
        }
    }

    private String texto(String value) {
        return value == null ? "" : value.trim();
    }
}