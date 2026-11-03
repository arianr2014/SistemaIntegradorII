package pe.edu.utp.novasalud.frontend.controller;

import com.google.gson.reflect.TypeToken;
import pe.edu.utp.novasalud.frontend.model.Cita;
import pe.edu.utp.novasalud.frontend.model.Especialidad;
import pe.edu.utp.novasalud.frontend.model.Medico;
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
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

/**
 * Agenda médica para Frontend Web Java.
 * Invoca la API REST del backend (`novasalud-backend-api`) mediante ApiClient.
 */
@WebServlet("/agenda-medico")
public class AgendaMedicoController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        cargarAgenda(request, response, null);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int idCita = parseEntero(request.getParameter("id"));
        if (idCita > 0) {
            response.sendRedirect(request.getContextPath() + "/atencion-medica?idCita=" + idCita);
        } else {
            response.sendRedirect(request.getContextPath() + "/agenda-medico?msg=invalid");
        }
    }

    private void cargarAgenda(HttpServletRequest request, HttpServletResponse response, String error)
            throws ServletException, IOException {

        String fecha = texto(request.getParameter("fecha"));
        int idMedico = parseEntero(request.getParameter("idMedico"));
        int idEspecialidad = parseEntero(request.getParameter("idEspecialidad"));
        boolean buscar = "1".equals(request.getParameter("buscar"));
        if (fecha.isBlank()) fecha = LocalDate.now().toString();

        try {
            HttpSession session = request.getSession(false);
            String rol = session == null ? "" : String.valueOf(session.getAttribute("rolUsuario"));
            boolean esDoctor = "DOCTOR".equalsIgnoreCase(rol);

            if (esDoctor) {
                idMedico = obtenerIdMedicoSesion(session);
                idEspecialidad = 0;
                buscar = true;
            }

            List<Medico> medicos = esDoctor ? new ArrayList<>() : ApiClient.getList("/medicos", new TypeToken<List<Medico>>(){}.getType());
            List<Especialidad> especialidades = esDoctor ? new ArrayList<>() : ApiClient.getList("/especialidades", new TypeToken<List<Especialidad>>(){}.getType());
            List<Cita> citas;
            if (esDoctor && idMedico <= 0) {
                citas = new ArrayList<>();
                request.setAttribute("error", "Su usuario no está asociado a un médico. Contacte al administrador.");
            } else {
                String query = "/citas/agenda?idMedico=" + idMedico + "&idEspecialidad=" + idEspecialidad + "&fecha=" + URLEncoder.encode(fecha, StandardCharsets.UTF_8);
                citas = buscar ? ApiClient.getList(query, new TypeToken<List<Cita>>(){}.getType()) : new ArrayList<>();
            }

            if (esDoctor && idMedico > 0) {
                YearMonth ym = YearMonth.from(LocalDate.parse(fecha));
                String inicioMes = ym.atDay(1).toString();
                String finMes = ym.atEndOfMonth().toString();

                List<String> diasMesAgenda = new ArrayList<>();
                for (int dia = 1; dia <= ym.lengthOfMonth(); dia++) {
                    diasMesAgenda.add(ym.atDay(dia).toString());
                }

                try {
                    String urlResumen = "/citas/resumen-mensual?idMedico=" + idMedico + "&inicio=" + inicioMes + "&fin=" + finMes;
                    java.util.Map<String, Double> resumenDouble = ApiClient.get(urlResumen, new TypeToken<java.util.Map<String, Double>>(){}.getType());
                    java.util.Map<String, Integer> resumenMensual = new java.util.HashMap<>();
                    if (resumenDouble != null) {
                        for (java.util.Map.Entry<String, Double> entry : resumenDouble.entrySet()) {
                            resumenMensual.put(entry.getKey(), entry.getValue() == null ? 0 : entry.getValue().intValue());
                        }
                    }
                    request.setAttribute("resumenMensual", resumenMensual);
                } catch (Exception ex) {
                    AppLogger.error(getClass(), "Error al obtener resumen mensual por médico", ex);
                    request.setAttribute("resumenMensual", new java.util.HashMap<String, Integer>());
                }

                request.setAttribute("diasMesAgenda", diasMesAgenda);
                request.setAttribute("mesAgenda", ym.toString());
            }

            request.setAttribute("medicos", medicos);
            request.setAttribute("especialidades", especialidades);
            request.setAttribute("citas", citas);
            request.setAttribute("idMedico", idMedico);
            request.setAttribute("idEspecialidad", idEspecialidad);
            request.setAttribute("fecha", fecha);
            request.setAttribute("buscar", buscar);
            request.setAttribute("modoDoctor", esDoctor);
            if (error != null) request.setAttribute("error", error);
            request.getRequestDispatcher("/WEB-INF/views/agenda/list.jsp").forward(request, response);
        } catch (Exception ex) {
            AppLogger.error(getClass(), "Error al consultar agenda médica vía REST API", ex);
            request.setAttribute("error", ex.getMessage());
            request.setAttribute("medicos", new ArrayList<Medico>());
            request.setAttribute("especialidades", new ArrayList<Especialidad>());
            request.setAttribute("citas", new ArrayList<Cita>());
            request.setAttribute("idMedico", idMedico);
            request.setAttribute("idEspecialidad", idEspecialidad);
            request.setAttribute("fecha", fecha);
            request.setAttribute("buscar", buscar);
            request.getRequestDispatcher("/WEB-INF/views/agenda/list.jsp").forward(request, response);
        }
    }

    private int obtenerIdMedicoSesion(HttpSession session) {
        if (session == null || session.getAttribute("idMedico") == null) return 0;
        try { return Integer.parseInt(String.valueOf(session.getAttribute("idMedico"))); } catch (NumberFormatException ex) { return 0; }
    }

    private int parseEntero(String value) { try { return Integer.parseInt(value); } catch (NumberFormatException ex) { return 0; } }
    private String texto(String value) { return value == null ? "" : value.trim(); }
}
