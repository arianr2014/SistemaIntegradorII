package pe.edu.utp.novasalud.frontend.controller;

import com.google.gson.reflect.TypeToken;
import pe.edu.utp.novasalud.frontend.model.Medico;
import pe.edu.utp.novasalud.frontend.model.Usuario;
import pe.edu.utp.novasalud.frontend.util.ApiClient;
import pe.edu.utp.novasalud.frontend.util.AppLogger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Controlador del módulo Usuarios para Frontend Web Java. */
@WebServlet("/usuarios")
public class UsuarioController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = texto(request.getParameter("accion"));
        try {
            if ("form".equals(accion) || "editar".equals(accion)) {
                abrirFormulario(request, response);
                return;
            }
            listar(request, response);
        } catch (Exception ex) {
            AppLogger.error(getClass(), "Error en módulo usuarios vía REST API", ex);
            throw new ServletException("No se pudo procesar el módulo usuarios.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String accion = texto(request.getParameter("accion"));
        try {
            int id = parseInt(request.getParameter("id"));
            if ("desactivar".equals(accion) || "activar".equals(accion)) {
                Map<String, String> body = new HashMap<>();
                body.put("estado", "desactivar".equals(accion) ? "INACTIVO" : "ACTIVO");
                body.put("auditoria", usuarioSesion(request));
                ApiClient.put("/usuarios/" + id + "/estado", body, Void.class);
                response.sendRedirect(request.getContextPath()+"/usuarios?msg=" + ("desactivar".equals(accion) ? "deactivated" : "activated"));
                return;
            }
            if ("reset".equals(accion)) {
                Map<String, String> body = new HashMap<>();
                body.put("password", texto(request.getParameter("passwordNuevo")));
                body.put("auditoria", usuarioSesion(request));
                ApiClient.put("/usuarios/" + id + "/password", body, Void.class);
                response.sendRedirect(request.getContextPath()+"/usuarios?msg=reset");
                return;
            }
            guardar(request, response);
        } catch (Exception ex) {
            AppLogger.error(getClass(), "Error guardando usuario vía REST API", ex);
            response.sendRedirect(request.getContextPath()+"/usuarios?msg=error");
        }
    }

    private void listar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String filtro = texto(request.getParameter("q"));
        String url = "/usuarios" + (filtro.isBlank() ? "" : "?filtro=" + URLEncoder.encode(filtro, StandardCharsets.UTF_8));
        List<Usuario> usuarios = ApiClient.getList(url, new TypeToken<List<Usuario>>(){}.getType());

        request.setAttribute("usuarios", usuarios);
        request.setAttribute("q", filtro);
        request.getRequestDispatcher("/WEB-INF/views/usuario/list.jsp").forward(request, response);
    }

    private void abrirFormulario(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int id = parseInt(request.getParameter("id"));
        if (id > 0) {
            Usuario u = ApiClient.get("/usuarios/" + id, Usuario.class);
            request.setAttribute("usuario", u);
        }
        List<Medico> medicos = ApiClient.getList("/medicos", new TypeToken<List<Medico>>(){}.getType());
        request.setAttribute("medicos", medicos);
        request.getRequestDispatcher("/WEB-INF/views/usuario/form.jsp").forward(request, response);
    }

    private void guardar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Usuario u = new Usuario();
        u.setIdUsuario(parseInt(request.getParameter("idUsuario")));
        u.setUsername(texto(request.getParameter("username")));
        u.setDni(texto(request.getParameter("dni")));
        u.setNombres(texto(request.getParameter("nombres")));
        u.setApellidos(texto(request.getParameter("apellidos")));
        u.setTelefono(texto(request.getParameter("telefono")));
        u.setCorreo(texto(request.getParameter("correo")));
        u.setCargo(texto(request.getParameter("cargo")));
        u.setRol(texto(request.getParameter("rol")));
        u.setEstadoRegistro(texto(request.getParameter("estadoRegistro")));
        int idMedico = parseInt(request.getParameter("idMedico"));
        u.setIdMedico(idMedico <= 0 ? null : idMedico);

        String passInicial = texto(request.getParameter("passwordInicial"));
        String auditoria = usuarioSesion(request);

        String url = "/usuarios?passwordInicial=" + URLEncoder.encode(passInicial, StandardCharsets.UTF_8)
                + "&auditoria=" + URLEncoder.encode(auditoria, StandardCharsets.UTF_8);

        ApiClient.post(url, u, Usuario.class);
        response.sendRedirect(request.getContextPath()+"/usuarios?msg=saved");
    }

    private String usuarioSesion(HttpServletRequest request) { HttpSession s = request.getSession(false); return s == null ? "sistema" : String.valueOf(s.getAttribute("usuarioLogeado")); }
    private String texto(String v) { return v == null ? "" : v.trim(); }
    private int parseInt(String v) { try { return Integer.parseInt(v); } catch(Exception e) { return 0; } }
}
