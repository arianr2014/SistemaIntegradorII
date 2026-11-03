package pe.edu.utp.novasalud.frontend.controller;

import com.google.gson.reflect.TypeToken;
import pe.edu.utp.novasalud.frontend.model.Horario;
import pe.edu.utp.novasalud.frontend.model.Medico;
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
 * Controlador del módulo Horarios para Frontend Web Java.
 * Invoca la API REST del backend (`novasalud-backend-api`) mediante ApiClient.
 */
@WebServlet("/horarios")
public class HorarioController extends HttpServlet {

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
                response.sendRedirect(request.getContextPath() + "/horarios?msg=metodo_invalido");
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

        guardarHorario(request, response);
    }

    private void listar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String q = texto(request.getParameter("q"));

        try {
            String url = "/horarios" + (q.isBlank() ? "" : "?filtro=" + URLEncoder.encode(q, StandardCharsets.UTF_8));
            List<Horario> horarios = ApiClient.getList(url, new TypeToken<List<Horario>>(){}.getType());
            request.setAttribute("horarios", horarios);
            request.setAttribute("q", q);
            request.getRequestDispatcher("/WEB-INF/views/horario/list.jsp").forward(request, response);

        } catch (Exception ex) {
            throw new ServletException("Error al listar horarios vía REST API", ex);
        }
    }

    private void abrirFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int idHorario = parseEntero(request.getParameter("id"));

        if (idHorario > 0) {
            try {
                Horario horario = ApiClient.get("/horarios/" + idHorario, Horario.class);

                if (horario == null) {
                    response.sendRedirect(request.getContextPath() + "/horarios?msg=noexiste");
                    return;
                }

                request.setAttribute("horario", horario);

            } catch (Exception ex) {
                throw new ServletException("Error al cargar horario vía REST API", ex);
            }
        }

        cargarMedicos(request);
        request.getRequestDispatcher("/WEB-INF/views/horario/form.jsp").forward(request, response);
    }

    private void guardarHorario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Horario horario = new Horario();
        horario.setIdHorario(parseEntero(request.getParameter("idHorario")));
        horario.setIdMedico(parseEntero(request.getParameter("idMedico")));
        horario.setDia(texto(request.getParameter("dia")));
        horario.setHoraInicio(texto(request.getParameter("horaInicio")));
        horario.setHoraFin(texto(request.getParameter("horaFin")));

        try {
            HttpSession session = request.getSession(false);
            String usuario = obtenerUsuarioSesion(session);

            ApiClient.post("/horarios?usuario=" + URLEncoder.encode(usuario, StandardCharsets.UTF_8), horario, Horario.class);
            response.sendRedirect(request.getContextPath() + "/horarios?msg=ok");

        } catch (Exception ex) {
            request.setAttribute("error", ex.getMessage());
            request.setAttribute("horario", horario);
            cargarMedicos(request);
            request.getRequestDispatcher("/WEB-INF/views/horario/form.jsp").forward(request, response);
        }
    }

    private void eliminar(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        int idHorario = parseEntero(request.getParameter("id"));

        if (idHorario <= 0) {
            response.sendRedirect(request.getContextPath() + "/horarios?msg=invalid");
            return;
        }

        try {
            boolean ok = ApiClient.delete("/horarios/" + idHorario);
            if (ok) {
                response.sendRedirect(request.getContextPath() + "/horarios?msg=deleted");
            } else {
                response.sendRedirect(request.getContextPath() + "/horarios?msg=invalid");
            }

        } catch (Exception ex) {
            response.sendRedirect(request.getContextPath() + "/horarios?msg=errorDelete");
        }
    }

    private void cargarMedicos(HttpServletRequest request) throws ServletException {
        try {
            List<Medico> medicos = ApiClient.getList("/medicos", new TypeToken<List<Medico>>(){}.getType());
            request.setAttribute("medicos", medicos);

        } catch (Exception ex) {
            throw new ServletException("No se pudo cargar la lista de medicos vía REST API", ex);
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