package pe.edu.utp.novasalud.frontend.controller;
/**
 * @author FRANCK
 */
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Controlador para mostrar una página personalizada de acceso denegado.
 *
 * Fase 4:
 * Refuerza el control de acceso por roles mostrando una respuesta clara
 * cuando un usuario intenta ingresar a un módulo no autorizado.
 */
@WebServlet("/acceso-denegado")
public class AccesoDenegadoController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        String rol = session == null ? "" : String.valueOf(session.getAttribute("rolUsuario"));
        String rutaBloqueada = texto(request.getParameter("from"));

        request.setAttribute("rolUsuario", rol);
        request.setAttribute("rutaBloqueada", limpiarParaVista(rutaBloqueada));
        request.setAttribute("rutaInicio", obtenerRutaInicioPorRol(rol));

        request.getRequestDispatcher("/WEB-INF/views/error/acceso-denegado.jsp").forward(request, response);
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

            default:
                return "/login";
        }
    }

    private String texto(String value) {
        return value == null ? "" : value.trim();
    }

    /**
     * Limpieza básica para evitar mostrar caracteres peligrosos en la vista.
     */
    private String limpiarParaVista(String value) {
        if (value == null || value.isBlank()) {
            return "Ruta no especificada";
        }

        return value
                .replace("<", "")
                .replace(">", "")
                .replace("\"", "")
                .replace("'", "");
    }
}