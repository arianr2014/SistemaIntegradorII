package pe.edu.utp.novasalud.frontend.controller;

import com.google.gson.reflect.TypeToken;
import pe.edu.utp.novasalud.frontend.model.Especialidad;
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
 * Controlador del módulo Médicos para Frontend Web Java.
 * Invoca la API REST del backend (`novasalud-backend-api`) mediante ApiClient.
 */
@WebServlet("/medicos")
public class MedicoController extends HttpServlet {

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
                response.sendRedirect(request.getContextPath() + "/medicos?msg=metodo_invalido");
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

        guardarMedico(request, response);
    }

    private void listar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String q = texto(request.getParameter("q"));

        try {
            String url = "/medicos" + (q.isBlank() ? "" : "?filtro=" + URLEncoder.encode(q, StandardCharsets.UTF_8));
            List<Medico> medicos = ApiClient.getList(url, new TypeToken<List<Medico>>(){}.getType());
            request.setAttribute("medicos", medicos);
            request.setAttribute("q", q);
            request.getRequestDispatcher("/WEB-INF/views/medico/list.jsp").forward(request, response);

        } catch (Exception ex) {
            throw new ServletException("Error al listar medicos vía REST API", ex);
        }
    }

    private void abrirFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int idMedico = parseEntero(request.getParameter("id"));

        if (idMedico > 0) {
            try {
                Medico medico = ApiClient.get("/medicos/" + idMedico, Medico.class);

                if (medico == null) {
                    response.sendRedirect(request.getContextPath() + "/medicos?msg=noexiste");
                    return;
                }

                request.setAttribute("medico", medico);

            } catch (Exception ex) {
                throw new ServletException("Error al cargar medico vía REST API", ex);
            }
        }

        cargarEspecialidades(request);
        request.getRequestDispatcher("/WEB-INF/views/medico/form.jsp").forward(request, response);
    }

    private void guardarMedico(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idTexto = request.getParameter("idMedico");
        String nombres = texto(request.getParameter("nombres"));
        String apellidos = texto(request.getParameter("apellidos"));
        String dni = texto(request.getParameter("dni"));
        String telefono = texto(request.getParameter("telefono"));
        String correo = texto(request.getParameter("correo"));
        String cmp = texto(request.getParameter("cmp"));
        String consultorio = texto(request.getParameter("consultorio"));
        int duracionCitaMinutos = parseEntero(request.getParameter("duracionCitaMinutos"));
        int toleranciaMinutos = parseEntero(request.getParameter("toleranciaMinutos"));
        int maxCitasDia = parseEntero(request.getParameter("maxCitasDia"));
        int idEspecialidad = parseEntero(request.getParameter("idEspecialidad"));

        Medico medico = new Medico();
        medico.setIdMedico(parseEntero(idTexto));
        medico.setNombres(nombres);
        medico.setApellidos(apellidos);
        medico.setDni(dni);
        medico.setTelefono(telefono);
        medico.setCorreo(correo);
        medico.setCmp(cmp);
        medico.setConsultorio(consultorio);
        medico.setDuracionCitaMinutos(duracionCitaMinutos);
        medico.setToleranciaMinutos(toleranciaMinutos);
        medico.setMaxCitasDia(maxCitasDia);
        medico.setIdEspecialidad(idEspecialidad);

        try {
            HttpSession session = request.getSession(false);
            String usuario = obtenerUsuarioSesion(session);

            ApiClient.post("/medicos?usuario=" + URLEncoder.encode(usuario, StandardCharsets.UTF_8), medico, Medico.class);
            response.sendRedirect(request.getContextPath() + "/medicos?msg=ok");

        } catch (Exception ex) {
            request.setAttribute("error", ex.getMessage());
            request.setAttribute("medico", medico);
            cargarEspecialidades(request);
            request.getRequestDispatcher("/WEB-INF/views/medico/form.jsp").forward(request, response);
        }
    }

    private void eliminar(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        int idMedico = parseEntero(request.getParameter("id"));

        if (idMedico <= 0) {
            response.sendRedirect(request.getContextPath() + "/medicos?msg=invalid");
            return;
        }

        try {
            boolean ok = ApiClient.delete("/medicos/" + idMedico);
            if (ok) {
                response.sendRedirect(request.getContextPath() + "/medicos?msg=deleted");
            } else {
                response.sendRedirect(request.getContextPath() + "/medicos?msg=invalid");
            }

        } catch (Exception ex) {
            response.sendRedirect(request.getContextPath() + "/medicos?msg=errorDelete");
        }
    }

    private void cargarEspecialidades(HttpServletRequest request) throws ServletException {
        try {
            List<Especialidad> especialidades = ApiClient.getList("/especialidades", new TypeToken<List<Especialidad>>(){}.getType());
            request.setAttribute("especialidades", especialidades);

        } catch (Exception ex) {
            throw new ServletException("No se pudo cargar la lista de especialidades vía REST API", ex);
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