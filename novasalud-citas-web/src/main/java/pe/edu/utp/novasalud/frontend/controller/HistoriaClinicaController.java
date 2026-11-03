package pe.edu.utp.novasalud.frontend.controller;

import com.google.gson.reflect.TypeToken;
import pe.edu.utp.novasalud.frontend.model.HistoriaClinica;
import pe.edu.utp.novasalud.frontend.util.ApiClient;
import pe.edu.utp.novasalud.frontend.util.AppLogger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet Controller para el Visor de Historias Clínicas y Detalle de Atenciones Médicas.
 */
@WebServlet({"/historias-clinicas", "/historia-clinica"})
public class HistoriaClinicaController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        validarAcceso(session, request, response);

        String idPacienteStr = request.getParameter("idPaciente");
        String codigoHistoria = request.getParameter("codigoHistoria");

        try {
            if ((idPacienteStr != null && !idPacienteStr.isBlank()) || (codigoHistoria != null && !codigoHistoria.isBlank())) {
                HistoriaClinica historia = null;
                if (idPacienteStr != null && !idPacienteStr.isBlank()) {
                    int idPaciente = parseEntero(idPacienteStr);
                    historia = ApiClient.get("/historia-clinica/paciente/" + idPaciente, HistoriaClinica.class);
                } else {
                    historia = ApiClient.get("/historia-clinica/codigo/" + URLEncoder.encode(codigoHistoria.trim(), StandardCharsets.UTF_8), HistoriaClinica.class);
                }

                if (historia == null) {
                    request.setAttribute("error", "No se encontró la historia clínica solicitada.");
                    response.sendRedirect(request.getContextPath() + "/historias-clinicas?error=notfound");
                    return;
                }

                request.setAttribute("historia", historia);
                request.getRequestDispatcher("/WEB-INF/views/historia/detail.jsp").forward(request, response);
            } else {
                String filtro = texto(request.getParameter("buscar"));
                String endpoint = "/historia-clinica" + (!filtro.isBlank() ? "?filtro=" + URLEncoder.encode(filtro, StandardCharsets.UTF_8) : "");
                Type listType = new TypeToken<List<HistoriaClinica>>() {}.getType();
                List<HistoriaClinica> historias = ApiClient.getList(endpoint, listType);

                request.setAttribute("historias", historias);
                request.setAttribute("filtro", filtro);
                request.getRequestDispatcher("/WEB-INF/views/historia/list.jsp").forward(request, response);
            }
        } catch (Exception ex) {
            AppLogger.error(getClass(), "Error al cargar módulo de Historia Clínica", ex);
            request.setAttribute("error", "Ocurrió un error al cargar la información: " + ex.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/historia/list.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        validarAcceso(session, request, response);

        String accion = texto(request.getParameter("accion"));
        int idPaciente = parseEntero(request.getParameter("idPaciente"));

        if ("actualizar_antecedentes".equals(accion)) {
            try {
                String antecedentes = texto(request.getParameter("antecedentesGenerales"));
                String alergias = texto(request.getParameter("alergias"));
                String usuarioAudit = String.valueOf(session.getAttribute("usuarioLogeado"));

                Map<String, String> payload = new HashMap<>();
                payload.put("antecedentesGenerales", antecedentes);
                payload.put("alergias", alergias);

                String endpoint = "/historia-clinica/paciente/" + idPaciente + "?usuarioAudit=" + URLEncoder.encode(usuarioAudit, StandardCharsets.UTF_8);
                ApiClient.put(endpoint, payload, String.class);

                response.sendRedirect(request.getContextPath() + "/historia-clinica?idPaciente=" + idPaciente + "&msg=updated");
            } catch (Exception ex) {
                AppLogger.error(getClass(), "Error al actualizar antecedentes en Historia Clínica", ex);
                response.sendRedirect(request.getContextPath() + "/historia-clinica?idPaciente=" + idPaciente + "&error=update_failed");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/historias-clinicas");
        }
    }

    private void validarAcceso(HttpSession session, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (session == null || session.getAttribute("usuarioLogeado") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }

    private int parseEntero(String val) {
        try { return Integer.parseInt(val); } catch (Exception ignored) { return 0; }
    }

    private String texto(String val) {
        return val == null ? "" : val.trim();
    }
}
