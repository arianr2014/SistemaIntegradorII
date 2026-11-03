package pe.edu.utp.novasalud.frontend.controller;

import com.google.gson.reflect.TypeToken;
import pe.edu.utp.novasalud.frontend.model.Cita;
import pe.edu.utp.novasalud.frontend.model.Especialidad;
import pe.edu.utp.novasalud.frontend.model.Medico;
import pe.edu.utp.novasalud.frontend.model.Paciente;
import pe.edu.utp.novasalud.frontend.model.TarifaConsulta;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador del módulo Citas para Frontend Web Java.
 * Invoca la API REST del backend (`novasalud-backend-api`) mediante ApiClient.
 */
@WebServlet("/citas")
public class CitaController extends HttpServlet {

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
            case "atender":
            case "eliminar":
                response.sendRedirect(request.getContextPath() + "/citas?msg=metodo_invalido");
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

        if ("atender".equals(accion)) {
            response.sendRedirect(request.getContextPath() + "/citas?msg=solo_doctor");
            return;
        }

        if ("eliminar".equals(accion)) {
            eliminar(request, response);
            return;
        }

        if ("noAsistio".equals(accion)) {
            noAsistio(request, response);
            return;
        }

        guardarCita(request, response);
    }

    private void listar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String dniPaciente = texto(request.getParameter("dniPaciente"));
        int idMedico = parseEntero(request.getParameter("idMedico"));
        int idEspecialidad = parseEntero(request.getParameter("idEspecialidad"));
        boolean buscar = "1".equals(request.getParameter("buscar"));

        try {
            List<Medico> medicos = ApiClient.getList("/medicos", new TypeToken<List<Medico>>(){}.getType());
            List<Especialidad> especialidades = ApiClient.getList("/especialidades", new TypeToken<List<Especialidad>>(){}.getType());

            List<Cita> citas = new ArrayList<>();
            if (buscar) {
                String query = "/citas?dniPaciente=" + URLEncoder.encode(dniPaciente, StandardCharsets.UTF_8)
                        + "&idMedico=" + idMedico + "&idEspecialidad=" + idEspecialidad;
                citas = ApiClient.getList(query, new TypeToken<List<Cita>>(){}.getType());
            }

            request.setAttribute("medicos", medicos);
            request.setAttribute("especialidades", especialidades);
            request.setAttribute("citas", citas);
            request.setAttribute("dniPaciente", dniPaciente);
            request.setAttribute("idMedico", idMedico);
            request.setAttribute("idEspecialidad", idEspecialidad);
            request.setAttribute("buscar", buscar);

            request.getRequestDispatcher("/WEB-INF/views/cita/list.jsp").forward(request, response);

        } catch (Exception ex) {
            throw new ServletException("Error al listar citas vía REST API", ex);
        }
    }

    private void abrirFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int idCita = parseEntero(request.getParameter("id"));
        int idEspecialidadSel = parseEntero(request.getParameter("idEspecialidadSel"));
        int idMedicoSel = parseEntero(request.getParameter("idMedicoSel"));
        if (idMedicoSel <= 0) {
            idMedicoSel = parseEntero(request.getParameter("idMedico"));
        }
        String dniBusqueda = texto(request.getParameter("dniBusqueda"));
        String fechaSel = texto(request.getParameter("fechaSel"));
        if (fechaSel.isBlank()) {
            fechaSel = texto(request.getParameter("fecha"));
        }
        String horaSel = texto(request.getParameter("horaSel"));
        if (horaSel.isBlank()) {
            horaSel = texto(request.getParameter("hora"));
        }

        Cita cita = null;

        if (idCita > 0) {
            try {
                cita = ApiClient.get("/citas/" + idCita, Cita.class);

                if (cita == null) {
                    response.sendRedirect(request.getContextPath() + "/citas?msg=noexiste");
                    return;
                }

                if ("ATENDIDA".equalsIgnoreCase(texto(cita.getEstado()))) {
                    response.sendRedirect(request.getContextPath() + "/citas?msg=readonly");
                    return;
                }

                request.setAttribute("cita", cita);

            } catch (Exception ex) {
                throw new ServletException("Error al cargar cita vía REST API", ex);
            }
        }

        int idPaciente = cita == null ? 0 : cita.getIdPaciente();
        int idMedico = idMedicoSel > 0 ? idMedicoSel : (cita == null ? 0 : cita.getIdMedico());

        if (fechaSel.isBlank() && cita != null && cita.getFecha() != null) {
            fechaSel = cita.getFecha();
        }

        if (horaSel.isBlank() && cita != null && cita.getHora() != null) {
            horaSel = cita.getHora();
        }

        if (idEspecialidadSel <= 0 && idMedico > 0) {
            try {
                Medico medico = ApiClient.get("/medicos/" + idMedico, Medico.class);
                if (medico != null) {
                    idEspecialidadSel = medico.getIdEspecialidad();
                }
            } catch (Exception ex) {
                throw new ServletException("No se pudo cargar especialidad del medico", ex);
            }
        }

        prepararFormulario(
                request,
                idPaciente,
                dniBusqueda,
                idEspecialidadSel,
                idMedico,
                fechaSel,
                horaSel,
                idCita
        );

        request.getRequestDispatcher("/WEB-INF/views/cita/form.jsp").forward(request, response);
    }

    private void guardarCita(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Cita cita = new Cita();
        cita.setIdCita(parseEntero(request.getParameter("idCita")));

        int idPaciente = parseEntero(request.getParameter("idPaciente"));
        String dniBusqueda = texto(request.getParameter("dniBusqueda"));

        if (idPaciente <= 0 && !dniBusqueda.isBlank()) {
            try {
                Paciente p = ApiClient.get("/pacientes/buscar/dni/" + URLEncoder.encode(dniBusqueda, StandardCharsets.UTF_8), Paciente.class);
                if (p != null) {
                    idPaciente = p.getIdPaciente();
                }
            } catch (Exception ignored) {}
        }

        cita.setIdPaciente(idPaciente);
        cita.setIdMedico(parseEntero(request.getParameter("idMedico")));
        cita.setFecha(texto(request.getParameter("fecha")));
        cita.setHora(texto(request.getParameter("hora")));
        cita.setEstado(texto(request.getParameter("estado")));
        cita.setObservaciones(texto(request.getParameter("observaciones")));

        int idEspecialidadSel = parseEntero(request.getParameter("idEspecialidadSel"));
        boolean esEdicion = cita.getIdCita() > 0;

        try {
            HttpSession session = request.getSession(false);
            String usuario = obtenerUsuarioSesion(session);
            int idUsuario = obtenerIdUsuarioSesion(session);

            String url = "/citas?usuario=" + URLEncoder.encode(usuario, StandardCharsets.UTF_8) + "&idRecepcionista=" + idUsuario;
            ApiClient.post(url, cita, Cita.class);

            if (esEdicion) {
                response.sendRedirect(request.getContextPath() + "/citas?msg=updated");
            } else {
                response.sendRedirect(request.getContextPath() + "/citas?msg=created");
            }

        } catch (Exception ex) {
            request.setAttribute("error", ex.getMessage());
            request.setAttribute("cita", cita);

            prepararFormulario(
                    request,
                    cita.getIdPaciente(),
                    dniBusqueda,
                    idEspecialidadSel,
                    cita.getIdMedico(),
                    cita.getFecha(),
                    cita.getHora(),
                    cita.getIdCita()
            );

            request.getRequestDispatcher("/WEB-INF/views/cita/form.jsp").forward(request, response);
        }
    }

    private void eliminar(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        int idCita = parseEntero(request.getParameter("id"));

        if (idCita <= 0) {
            response.sendRedirect(request.getContextPath() + "/citas?msg=invalid");
            return;
        }

        try {
            boolean ok = ApiClient.delete("/citas/" + idCita);
            if (ok) {
                response.sendRedirect(request.getContextPath() + "/citas?msg=deleted");
            } else {
                response.sendRedirect(request.getContextPath() + "/citas?msg=invalid");
            }

        } catch (Exception ex) {
            response.sendRedirect(request.getContextPath() + "/citas?msg=errorDelete");
        }
    }

    private void noAsistio(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int idCita = parseEntero(request.getParameter("id"));

        if (idCita <= 0) {
            response.sendRedirect(request.getContextPath() + "/citas?msg=invalid");
            return;
        }

        try {
            ApiClient.put("/citas/" + idCita + "/no-asistio", null, Void.class);
            response.sendRedirect(request.getContextPath() + "/citas?msg=no_asistio");
        } catch (Exception ex) {
            response.sendRedirect(request.getContextPath() + "/citas?msg=errorUpdate");
        }
    }

    private void prepararFormulario(
            HttpServletRequest request,
            int idPaciente,
            String dniBusqueda,
            int idEspecialidadSel,
            int idMedicoSel,
            String fechaSel,
            String horaSel,
            int idCitaActual
    ) throws ServletException {

        try {
            Paciente pacienteEncontrado = null;

            if (!dniBusqueda.isBlank()) {
                try {
                    pacienteEncontrado = ApiClient.get("/pacientes/buscar/dni/" + URLEncoder.encode(dniBusqueda, StandardCharsets.UTF_8), Paciente.class);
                } catch (Exception ignored) {}
            }

            if (pacienteEncontrado == null && idPaciente > 0) {
                try {
                    pacienteEncontrado = ApiClient.get("/pacientes/" + idPaciente, Paciente.class);
                } catch (Exception ignored) {}
            }

            List<Especialidad> especialidades = ApiClient.getList("/especialidades", new TypeToken<List<Especialidad>>(){}.getType());
            
            // Si hay especialidad seleccionada, filtrar médicos por especialidad
            List<Medico> medicos = new ArrayList<>();
            if (idEspecialidadSel > 0) {
                for (Medico m : ApiClient.<Medico>getList("/medicos", new TypeToken<List<Medico>>(){}.getType())) {
                    if (m.getIdEspecialidad() == idEspecialidadSel) {
                        medicos.add(m);
                    }
                }
            } else {
                medicos = ApiClient.getList("/medicos", new TypeToken<List<Medico>>(){}.getType());
            }

            List<TarifaConsulta> tarifas = ApiClient.getList("/tarifas", new TypeToken<List<TarifaConsulta>>(){}.getType());

            // Carga dinámica de horarios disponibles vía API REST
            List<String> horariosDisponibles = new ArrayList<>();
            if (idMedicoSel > 0 && !fechaSel.isBlank()) {
                try {
                    String query = "/horarios/disponibles?idMedico=" + idMedicoSel
                            + "&fecha=" + URLEncoder.encode(fechaSel, StandardCharsets.UTF_8)
                            + "&idCitaExcluir=" + idCitaActual;
                    horariosDisponibles = ApiClient.getList(query, new TypeToken<List<String>>(){}.getType());
                } catch (Exception ignored) {}
            }

            request.setAttribute("pacienteEncontrado", pacienteEncontrado);
            request.setAttribute("dniBusqueda", dniBusqueda);
            request.setAttribute("especialidades", especialidades);
            request.setAttribute("medicos", medicos);
            request.setAttribute("horariosDisponibles", horariosDisponibles);
            request.setAttribute("tarifasActivas", tarifas);
            request.setAttribute("idEspecialidadSel", idEspecialidadSel);
            request.setAttribute("idMedicoSel", idMedicoSel);
            request.setAttribute("fechaSel", fechaSel);
            request.setAttribute("horaSel", horaSel);

        } catch (Exception ex) {
            throw new ServletException("No se pudo preparar el formulario de citas vía REST API", ex);
        }
    }

    private String obtenerUsuarioSesion(HttpSession session) {
        if (session == null || session.getAttribute("usuarioLogeado") == null) {
            return "sistema";
        }
        return String.valueOf(session.getAttribute("usuarioLogeado"));
    }

    private int obtenerIdUsuarioSesion(HttpSession session) {
        if (session == null || session.getAttribute("idUsuario") == null) {
            return 0;
        }
        try {
            return Integer.parseInt(String.valueOf(session.getAttribute("idUsuario")));
        } catch (NumberFormatException ex) {
            return 0;
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