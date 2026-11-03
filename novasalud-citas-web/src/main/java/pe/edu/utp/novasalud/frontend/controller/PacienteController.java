package pe.edu.utp.novasalud.frontend.controller;

import com.google.gson.reflect.TypeToken;
import pe.edu.utp.novasalud.frontend.model.Paciente;
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
 * Controlador del módulo Pacientes para Frontend Web Java.
 * Invoca la API REST del backend (`novasalud-backend-api`) mediante ApiClient.
 */
@WebServlet("/pacientes")
public class PacienteController extends HttpServlet {

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
                response.sendRedirect(request.getContextPath() + "/pacientes?msg=metodo_invalido");
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

        guardarPaciente(request, response);
    }

    private void listar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String q = texto(request.getParameter("q"));

        try {
            String url = "/pacientes" + (q.isBlank() ? "" : "?filtro=" + URLEncoder.encode(q, StandardCharsets.UTF_8));
            List<Paciente> pacientes = ApiClient.getList(url, new TypeToken<List<Paciente>>(){}.getType());
            request.setAttribute("pacientes", pacientes);
            request.setAttribute("q", q);
            request.getRequestDispatcher("/WEB-INF/views/paciente/list.jsp").forward(request, response);
        } catch (Exception ex) {
            throw new ServletException("Error al listar pacientes vía REST API", ex);
        }
    }

    private void abrirFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int idPaciente = parseEntero(request.getParameter("id"));

        if (idPaciente > 0) {
            try {
                Paciente paciente = ApiClient.get("/pacientes/" + idPaciente, Paciente.class);

                if (paciente == null) {
                    response.sendRedirect(request.getContextPath() + "/pacientes?msg=noexiste");
                    return;
                }

                request.setAttribute("paciente", paciente);

            } catch (Exception ex) {
                throw new ServletException("Error al cargar paciente vía REST API", ex);
            }
        }

        request.getRequestDispatcher("/WEB-INF/views/paciente/form.jsp").forward(request, response);
    }

    private void guardarPaciente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idTexto = request.getParameter("idPaciente");
        String dni = texto(request.getParameter("dni"));
        String nombres = texto(request.getParameter("nombres"));
        String apellidos = texto(request.getParameter("apellidos"));
        String telefono = texto(request.getParameter("telefono"));
        String correo = texto(request.getParameter("correo"));
        String direccion = texto(request.getParameter("direccion"));
        String fechaNacimiento = texto(request.getParameter("fechaNacimiento"));
        String sexo = texto(request.getParameter("sexo"));
        String contactoEmergencia = texto(request.getParameter("contactoEmergencia"));
        String telefonoEmergencia = texto(request.getParameter("telefonoEmergencia"));

        Paciente paciente = new Paciente();
        paciente.setIdPaciente(parseEntero(idTexto));
        paciente.setDni(dni);
        paciente.setNombres(nombres);
        paciente.setApellidos(apellidos);
        paciente.setTelefono(telefono);
        paciente.setCorreo(correo);
        paciente.setDireccion(direccion);
        paciente.setFechaNacimiento(fechaNacimiento);
        paciente.setSexo(sexo);
        paciente.setContactoEmergencia(contactoEmergencia);
        paciente.setTelefonoEmergencia(telefonoEmergencia);

        try {
            HttpSession session = request.getSession(false);
            String usuario = session == null ? "sistema" : String.valueOf(session.getAttribute("usuarioLogeado"));

            ApiClient.post("/pacientes?usuario=" + URLEncoder.encode(usuario, StandardCharsets.UTF_8), paciente, Paciente.class);
            response.sendRedirect(request.getContextPath() + "/pacientes?msg=ok");

        } catch (Exception ex) {
            request.setAttribute("error", ex.getMessage());
            request.setAttribute("paciente", paciente);
            request.getRequestDispatcher("/WEB-INF/views/paciente/form.jsp").forward(request, response);
        }
    }

    private void eliminar(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        int idPaciente = parseEntero(request.getParameter("id"));

        if (idPaciente <= 0) {
            response.sendRedirect(request.getContextPath() + "/pacientes?msg=invalid");
            return;
        }

        try {
            boolean ok = ApiClient.delete("/pacientes/" + idPaciente);
            if (ok) {
                response.sendRedirect(request.getContextPath() + "/pacientes?msg=deleted");
            } else {
                response.sendRedirect(request.getContextPath() + "/pacientes?msg=invalid");
            }

        } catch (Exception ex) {
            throw new ServletException("Error al eliminar paciente vía REST API", ex);
        }
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