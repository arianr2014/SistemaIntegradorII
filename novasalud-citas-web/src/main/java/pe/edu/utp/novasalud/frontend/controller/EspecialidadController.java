package pe.edu.utp.novasalud.frontend.controller;

import com.google.gson.reflect.TypeToken;
import pe.edu.utp.novasalud.frontend.model.Especialidad;
import pe.edu.utp.novasalud.frontend.util.ApiClient;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Controlador del módulo Especialidades para Frontend Web Java.
 * Invoca la API REST del backend (`novasalud-backend-api`) mediante ApiClient.
 */
@WebServlet("/especialidades")
public class EspecialidadController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = texto(request.getParameter("accion"));

        if (accion.isBlank() || "listar".equals(accion)) {
            listar(request, response);
            return;
        }

        switch (accion) {
            case "form":
            case "editar":
                abrirFormulario(request, response);
                break;
            case "eliminar":
                response.sendRedirect(request.getContextPath() + "/especialidades?msg=metodo_invalido");
                break;
            default:
                listar(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String accion = texto(request.getParameter("accion"));

        if ("eliminar".equals(accion)) {
            eliminar(request, response);
            return;
        }

        guardarEspecialidad(request, response);
    }

    private void listar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String q = texto(request.getParameter("q"));

        try {
            String url = "/especialidades" + (q.isBlank() ? "" : "?filtro=" + URLEncoder.encode(q, StandardCharsets.UTF_8));
            List<Especialidad> especialidades = ApiClient.getList(url, new TypeToken<List<Especialidad>>(){}.getType());
            request.setAttribute("especialidades", especialidades);
            request.setAttribute("q", q);
            request.getRequestDispatcher("/WEB-INF/views/especialidad/list.jsp").forward(request, response);

        } catch (Exception ex) {
            throw new ServletException("Error al listar especialidades vía REST API", ex);
        }
    }

    private void abrirFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int idEspecialidad = parseEntero(request.getParameter("id"));

        if (idEspecialidad > 0) {
            try {
                Especialidad especialidad = ApiClient.get("/especialidades/" + idEspecialidad, Especialidad.class);

                if (especialidad == null) {
                    response.sendRedirect(request.getContextPath() + "/especialidades?msg=noexiste");
                    return;
                }

                request.setAttribute("especialidad", especialidad);

            } catch (Exception ex) {
                throw new ServletException("Error al cargar especialidad vía REST API", ex);
            }
        }

        request.getRequestDispatcher("/WEB-INF/views/especialidad/form.jsp").forward(request, response);
    }

    private void guardarEspecialidad(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Especialidad especialidad = new Especialidad();
        especialidad.setIdEspecialidad(parseEntero(request.getParameter("idEspecialidad")));
        especialidad.setNombre(texto(request.getParameter("nombre")));
        especialidad.setDescripcion(texto(request.getParameter("descripcion")));

        try {
            HttpSession session = request.getSession(false);
            String usuario = obtenerUsuarioSesion(session);

            ApiClient.post("/especialidades?usuario=" + URLEncoder.encode(usuario, StandardCharsets.UTF_8), especialidad, Especialidad.class);
            response.sendRedirect(request.getContextPath() + "/especialidades?msg=ok");

        } catch (Exception ex) {
            request.setAttribute("error", ex.getMessage());
            request.setAttribute("especialidad", especialidad);
            request.getRequestDispatcher("/WEB-INF/views/especialidad/form.jsp").forward(request, response);
        }
    }

    private void eliminar(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        int idEspecialidad = parseEntero(request.getParameter("id"));

        if (idEspecialidad <= 0) {
            response.sendRedirect(request.getContextPath() + "/especialidades?msg=invalid");
            return;
        }

        try {
            boolean ok = ApiClient.delete("/especialidades/" + idEspecialidad);
            if (ok) {
                response.sendRedirect(request.getContextPath() + "/especialidades?msg=deleted");
            } else {
                response.sendRedirect(request.getContextPath() + "/especialidades?msg=invalid");
            }

        } catch (Exception ex) {
            response.sendRedirect(request.getContextPath() + "/especialidades?msg=errorDelete");
        }
    }

    private String obtenerUsuarioSesion(HttpSession session) {
        if (session == null || session.getAttribute("usuarioLogeado") == null) {
            return "sistema";
        }

        return String.valueOf(session.getAttribute("usuarioLogeado"));
    }

    private int parseEntero(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private String texto(String value) {
        return value == null ? "" : value.trim();
    }
}