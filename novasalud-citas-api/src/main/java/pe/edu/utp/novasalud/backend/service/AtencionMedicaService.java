package pe.edu.utp.novasalud.backend.service;

import pe.edu.utp.novasalud.backend.dao.AtencionMedicaDAO;
import pe.edu.utp.novasalud.backend.dao.HistoriaClinicaDAO;
import pe.edu.utp.novasalud.backend.model.AtencionMedica;
import pe.edu.utp.novasalud.backend.model.Cita;
import pe.edu.utp.novasalud.backend.util.ConexionDB;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/** Reglas de negocio para la atención médica con transaccionalidad JDBC e historia clínica. */
public class AtencionMedicaService {

    private final AtencionMedicaDAO atencionDAO = new AtencionMedicaDAO();
    private final HistoriaClinicaDAO historiaDAO = new HistoriaClinicaDAO();
    private final CitaService citaService = new CitaService();

    public AtencionMedica obtenerPorId(int idAtencion) throws SQLException {
        if (idAtencion <= 0) return null;
        return atencionDAO.obtenerPorId(idAtencion);
    }

    public AtencionMedica obtenerPorCita(int idCita) throws SQLException {
        if (idCita <= 0) return null;
        return atencionDAO.obtenerPorCita(idCita);
    }

    public List<AtencionMedica> listarPorPaciente(int idPaciente) throws SQLException {
        if (idPaciente <= 0) return List.of();
        return atencionDAO.listarPorPaciente(idPaciente);
    }

    public void guardar(AtencionMedica atencion, int idMedicoSesion) throws SQLException {
        validar(atencion, idMedicoSesion);
        try (Connection cn = ConexionDB.getConnection()) {
            historiaDAO.asegurarExisteHistoriaClinica(cn, atencion.getIdPaciente(), atencion.getCodigoHistoria(), String.valueOf(idMedicoSesion));
            atencionDAO.guardarOActualizarConConexion(cn, atencion);
        }
    }

    public void guardarYMarcarAtendida(AtencionMedica atencion, int idMedicoSesion) throws SQLException {
        validar(atencion, idMedicoSesion);
        try (Connection cn = ConexionDB.getConnection()) {
            cn.setAutoCommit(false);
            try {
                historiaDAO.asegurarExisteHistoriaClinica(cn, atencion.getIdPaciente(), atencion.getCodigoHistoria(), String.valueOf(idMedicoSesion));
                atencionDAO.guardarOActualizarConConexion(cn, atencion);
                boolean ok = citaService.marcarComoAtendidaPorDoctorConConexion(cn, atencion.getIdCita(), idMedicoSesion);
                if (!ok) {
                    throw new SQLException("No se pudo marcar la cita como ATENDIDA.");
                }
                cn.commit();
            } catch (SQLException ex) {
                cn.rollback();
                throw ex;
            } finally {
                cn.setAutoCommit(true);
            }
        }
    }

    private void validar(AtencionMedica atencion, int idMedicoSesion) throws SQLException {
        if (atencion == null) throw new IllegalArgumentException("No se recibió información de la atención médica.");
        if (idMedicoSesion <= 0) throw new IllegalArgumentException("Su usuario no está vinculado a un médico real.");
        if (atencion.getIdCita() <= 0) throw new IllegalArgumentException("La cita es obligatoria.");

        Cita cita = citaService.obtenerPorId(atencion.getIdCita());
        if (cita == null || cita.getIdMedico() != idMedicoSesion) {
            throw new IllegalArgumentException("No tiene permiso para registrar la atención de esta cita.");
        }
        if (!"PAGADO".equalsIgnoreCase(cita.getEstadoPago())) {
            throw new IllegalStateException("La atención clínica solo puede registrarse cuando la cita está pagada.");
        }
        if (isBlank(atencion.getMotivoConsulta()) || isBlank(atencion.getSintomas()) || isBlank(atencion.getDiagnostico()) || isBlank(atencion.getTratamiento())) {
            throw new IllegalArgumentException("Motivo, síntomas, diagnóstico y tratamiento son obligatorios antes de cerrar la atención.");
        }
        atencion.setIdMedico(cita.getIdMedico());
        atencion.setIdPaciente(cita.getIdPaciente());
        atencion.setCodigoHistoria(cita.getHistoriaClinicaCodigo());
    }

    private boolean isBlank(String value) { return value == null || value.isBlank(); }
}
