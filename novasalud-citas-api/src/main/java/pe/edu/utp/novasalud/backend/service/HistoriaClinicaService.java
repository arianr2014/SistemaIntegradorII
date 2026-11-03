package pe.edu.utp.novasalud.backend.service;

import pe.edu.utp.novasalud.backend.dao.AtencionMedicaDAO;
import pe.edu.utp.novasalud.backend.dao.HistoriaClinicaDAO;
import pe.edu.utp.novasalud.backend.model.AtencionMedica;
import pe.edu.utp.novasalud.backend.model.HistoriaClinica;
import java.sql.SQLException;
import java.util.List;

/** Servicio de Lógica de Negocio para Historia Clínica. */
public class HistoriaClinicaService {

    private final HistoriaClinicaDAO historiaDAO = new HistoriaClinicaDAO();
    private final AtencionMedicaDAO atencionDAO = new AtencionMedicaDAO();

    public HistoriaClinica obtenerHistoriaConAtencionesPorPaciente(int idPaciente) throws SQLException {
        if (idPaciente <= 0) return null;
        HistoriaClinica historia = historiaDAO.obtenerPorIdPaciente(idPaciente);
        if (historia != null) {
            List<AtencionMedica> atenciones = atencionDAO.listarPorPaciente(idPaciente);
            historia.setAtenciones(atenciones);
        }
        return historia;
    }

    public HistoriaClinica obtenerHistoriaConAtencionesPorCodigo(String codigoHistoria) throws SQLException {
        if (codigoHistoria == null || codigoHistoria.isBlank()) return null;
        HistoriaClinica historia = historiaDAO.obtenerPorCodigo(codigoHistoria.trim());
        if (historia != null) {
            List<AtencionMedica> atenciones = atencionDAO.listarPorCodigoHistoria(codigoHistoria.trim());
            historia.setAtenciones(atenciones);
        }
        return historia;
    }

    public List<HistoriaClinica> listarHistorias(String filtro) throws SQLException {
        return historiaDAO.listarHistorias(filtro);
    }

    public boolean actualizarAntecedentesYAlergias(int idPaciente, String antecedentes, String alergias, String usuarioAudit) throws SQLException {
        if (idPaciente <= 0) throw new IllegalArgumentException("El ID del paciente es inválido.");
        return historiaDAO.actualizarAntecedentesYAlergias(idPaciente, antecedentes, alergias, usuarioAudit);
    }
}
