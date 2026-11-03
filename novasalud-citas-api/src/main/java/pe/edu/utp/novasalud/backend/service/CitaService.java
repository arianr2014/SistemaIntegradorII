package pe.edu.utp.novasalud.backend.service;

import pe.edu.utp.novasalud.backend.dao.CitaDAO;
import pe.edu.utp.novasalud.backend.dao.CitaDAOImpl;
import pe.edu.utp.novasalud.backend.dao.HorarioDAO;
import pe.edu.utp.novasalud.backend.dao.HorarioDAOImpl;
import pe.edu.utp.novasalud.backend.dao.MedicoDAO;
import pe.edu.utp.novasalud.backend.dao.MedicoDAOImpl;
import pe.edu.utp.novasalud.backend.dao.PagoDAO;
import pe.edu.utp.novasalud.backend.dao.TarifaConsultaDAO;
import pe.edu.utp.novasalud.backend.model.Cita;
import pe.edu.utp.novasalud.backend.model.TarifaConsulta;
import pe.edu.utp.novasalud.backend.model.Medico;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Reglas de negocio de citas. */
public class CitaService {

    private static final Set<String> ESTADOS_RECEPCION = new HashSet<>(Arrays.asList(
            "PROGRAMADA", "CONFIRMADA", "REPROGRAMADA", "CANCELADA", "NO_ASISTIO", "EN_ESPERA"
    ));

    private final CitaDAO citaDAO = new CitaDAOImpl();
    private final HorarioDAO horarioDAO = new HorarioDAOImpl();
    private final MedicoDAO medicoDAO = new MedicoDAOImpl();
    private final TarifaConsultaDAO tarifaDAO = new TarifaConsultaDAO();
    private final PagoDAO pagoDAO = new PagoDAO();

    public List<Cita> listarFiltrado(String dniPaciente, int idMedico, int idEspecialidad) throws SQLException {
        return citaDAO.listarFiltrado(dniPaciente, idMedico, idEspecialidad);
    }

    public List<Cita> listarAgendaProgramada(int idMedico, String fecha) throws SQLException {
        return listarAgendaProgramadaFlexible(idMedico, 0, fecha);
    }

    public List<Cita> listarAgendaProgramadaFlexible(int idMedico, int idEspecialidad, String fecha) throws SQLException {
        if (isBlank(fecha)) throw new IllegalArgumentException("Debe seleccionar una fecha.");
        validarFechaFormato(fecha);
        return citaDAO.listarAgendaProgramadaFlexible(idMedico, idEspecialidad, fecha.trim());
    }

    public Cita obtenerPorId(int idCita) throws SQLException { return idCita <= 0 ? null : citaDAO.obtenerPorId(idCita); }

    public java.util.Map<String,Integer> resumenMensualPorMedico(int idMedico, String inicioMes, String finMes) throws SQLException {
        return citaDAO.resumenMensualPorMedico(idMedico, inicioMes, finMes);
    }

    public void guardar(Cita cita, String usuarioRegistro, int idUsuarioRecepcionista) throws SQLException {
        validarCita(cita);
        boolean nueva = cita.getIdCita() <= 0;

        if (nueva) {
            cita.setEstado("PROGRAMADA");
            cita.setIdUsuarioRecepcionista(idUsuarioRecepcionista > 0 ? idUsuarioRecepcionista : null);
            aplicarTarifaVigente(cita);
        } else {
            Cita citaOriginal = citaDAO.obtenerPorId(cita.getIdCita());
            if (citaOriginal == null) {
                throw new IllegalArgumentException("La cita que intenta actualizar no existe.");
            }

            String estado = normalizar(cita.getEstado());
            if (!ESTADOS_RECEPCION.contains(estado)) {
                throw new IllegalArgumentException("Recepción solo puede programar, reprogramar, cancelar, marcar en espera o no asistió. La atención la registra el médico.");
            }
            cita.setEstado(estado);

            boolean cambioMedico = citaOriginal.getIdMedico() != cita.getIdMedico();
            String estadoPago = citaOriginal.getEstadoPago() == null ? "PENDIENTE" : citaOriginal.getEstadoPago().trim().toUpperCase();
            if (cambioMedico && !"PENDIENTE".equals(estadoPago)) {
                throw new IllegalArgumentException("No se puede cambiar el médico de una cita con pago parcial o pagado. Anule o reprograme administrativamente.");
            }

            if (cambioMedico) {
                aplicarTarifaVigente(cita);
            } else {
                cita.setIdEspecialidad(citaOriginal.getIdEspecialidad());
                cita.setIdTarifaAplicada(citaOriginal.getIdTarifaAplicada());
                cita.setMontoConsultaDecimal(citaOriginal.getMontoConsultaDecimal());
            }
        }

        validarCapacidadDiaria(cita);
        validarDisponibilidadHoraria(cita);
        citaDAO.guardar(cita, usuarioRegistro == null || usuarioRegistro.isBlank() ? "sistema" : usuarioRegistro);

        if (nueva && cita.getIdCita() > 0) {
            pagoDAO.crearPagoPendienteSiNoExiste(cita.getIdCita(), cita.getMontoConsultaDecimal(), usuarioRegistro);
        } else if (!nueva) {
            pagoDAO.actualizarMontoPendienteSinAbonos(cita.getIdCita(), cita.getMontoConsultaDecimal());
        }
    }

    public void guardar(Cita cita, String usuarioRegistro) throws SQLException {
        guardar(cita, usuarioRegistro, 0);
    }

    public void eliminar(int idCita) throws SQLException {
        if (idCita <= 0) throw new IllegalArgumentException("El id de la cita no es válido.");
        citaDAO.eliminar(idCita);
    }

    public boolean marcarNoAsistio(int idCita) throws SQLException {
        if (idCita <= 0) throw new IllegalArgumentException("El id de la cita no es válido.");
        return citaDAO.actualizarEstado(idCita, "NO_ASISTIO");
    }

    public boolean marcarComoAtendida(int idCita) throws SQLException {
        if (idCita <= 0) throw new IllegalArgumentException("El id de la cita no es válido.");
        Cita cita = citaDAO.obtenerPorId(idCita);
        if (cita == null) throw new IllegalArgumentException("La cita no existe.");
        return marcarAtendidaValidada(idCita, cita.getIdMedico());
    }

    public boolean marcarComoAtendidaPorDoctor(int idCita, int idMedicoSesion) throws SQLException {
        if (idCita <= 0) throw new IllegalArgumentException("El id de la cita no es válido.");
        if (idMedicoSesion <= 0) throw new IllegalArgumentException("Su usuario no está asociado a un médico. Contacte al administrador.");
        Cita cita = citaDAO.obtenerPorId(idCita);
        if (cita == null || cita.getIdMedico() != idMedicoSesion) {
            throw new IllegalArgumentException("No tiene permiso para atender esta cita.");
        }
        return marcarAtendidaValidada(idCita, idMedicoSesion);
    }

    public boolean marcarComoAtendidaPorDoctorConConexion(Connection connection, int idCita, int idMedicoSesion) throws SQLException {
        if (idCita <= 0) throw new IllegalArgumentException("El id de la cita no es válido.");
        if (idMedicoSesion <= 0) throw new IllegalArgumentException("Su usuario no está asociado a un médico. Contacte al administrador.");
        Cita cita = citaDAO.obtenerPorId(idCita);
        if (cita == null || cita.getIdMedico() != idMedicoSesion) {
            throw new IllegalArgumentException("No tiene permiso para atender esta cita.");
        }
        String estadoPago = pagoDAO.obtenerEstadoPagoPorCita(idCita);
        if (!"PAGADO".equalsIgnoreCase(estadoPago)) {
            throw new IllegalStateException("La cita debe estar pagada antes de ingresar a consulta.");
        }
        return citaDAO.actualizarEstadoPorMedicoConConexion(connection, idCita, idMedicoSesion, "ATENDIDA");
    }

    private boolean marcarAtendidaValidada(int idCita, int idMedico) throws SQLException {
        String estadoPago = pagoDAO.obtenerEstadoPagoPorCita(idCita);
        if (!"PAGADO".equalsIgnoreCase(estadoPago)) {
            throw new IllegalStateException("La cita debe estar pagada antes de ingresar a consulta.");
        }
        return citaDAO.actualizarEstadoPorMedico(idCita, idMedico, "ATENDIDA");
    }

    private void aplicarTarifaVigente(Cita cita) throws SQLException {
        TarifaConsulta tarifa = tarifaDAO.obtenerTarifaVigenteDetallePorMedico(cita.getIdMedico(), cita.getFecha());
        if (tarifa == null || tarifa.getMonto() == null || tarifa.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("No existe tarifa vigente para la especialidad del médico seleccionado.");
        }
        cita.setIdEspecialidad(tarifa.getIdEspecialidad());
        cita.setIdTarifaAplicada(tarifa.getIdTarifa());
        cita.setMontoConsultaDecimal(tarifa.getMonto());
    }

    private void validarCita(Cita cita) {
        if (cita == null) throw new IllegalArgumentException("No se recibió información de la cita.");
        if (cita.getIdPaciente() <= 0 || cita.getIdMedico() <= 0) throw new IllegalArgumentException("Paciente y médico son obligatorios.");
        if (isBlank(cita.getFecha()) || isBlank(cita.getHora())) throw new IllegalArgumentException("Fecha y hora son obligatorias.");
        validarFecha(cita.getFecha());
    }

    private void validarFecha(String fecha) {
        try {
            LocalDate fechaCita = LocalDate.parse(fecha);
            if (fechaCita.isBefore(LocalDate.now())) throw new IllegalArgumentException("No se permite registrar citas en fechas pasadas.");
        } catch (DateTimeParseException ex) { throw new IllegalArgumentException("El formato de la fecha es inválido."); }
    }

    private void validarFechaFormato(String fecha) {
        try { LocalDate.parse(fecha); } catch (DateTimeParseException ex) { throw new IllegalArgumentException("La fecha seleccionada no tiene un formato válido."); }
    }

    private void validarCapacidadDiaria(Cita cita) throws SQLException {
        Medico medico = medicoDAO.obtenerPorId(cita.getIdMedico());
        if (medico == null) throw new IllegalArgumentException("El médico seleccionado no existe o está inactivo.");
        cita.setDuracionMinutos(medico.getDuracionCitaMinutos() <= 0 ? 30 : medico.getDuracionCitaMinutos());
        int programadas = citaDAO.contarCitasActivasMedicoFecha(cita.getIdMedico(), cita.getFecha(), cita.getIdCita());
        int maximo = medico.getMaxCitasDia() <= 0 ? 12 : medico.getMaxCitasDia();
        if (programadas >= maximo) {
            throw new IllegalArgumentException("El médico alcanzó su máximo de atenciones para la fecha seleccionada. Elija otro día u otro médico.");
        }
    }

    private void validarDisponibilidadHoraria(Cita cita) throws SQLException {
        List<String> horasDisponibles = horarioDAO.listarHorasDisponibles(cita.getIdMedico(), cita.getFecha(), cita.getIdCita());
        if (horasDisponibles.isEmpty()) throw new IllegalArgumentException("El médico no tiene horarios disponibles para la fecha seleccionada.");
        String hora = cita.getHora() == null ? "" : cita.getHora().trim();
        if (!horasDisponibles.contains(hora)) throw new IllegalArgumentException("La hora seleccionada ya no está disponible. Recargue y elija otro horario.");
    }

    private String normalizar(String value) { return value == null ? "" : value.trim().toUpperCase(); }
    private boolean isBlank(String value) { return value == null || value.isBlank(); }
}
