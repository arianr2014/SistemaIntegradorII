package pe.edu.utp.novasalud.backend.service;

import pe.edu.utp.novasalud.backend.dao.MedicoDAO;
import pe.edu.utp.novasalud.backend.dao.MedicoDAOImpl;
import pe.edu.utp.novasalud.backend.model.Medico;
import java.sql.SQLException;
import java.util.List;

public class MedicoService {

    private final MedicoDAO medicoDAO = new MedicoDAOImpl();

    public List<Medico> listar(String filtro) throws SQLException {
        return medicoDAO.listar(filtro);
    }

    public Medico obtenerPorId(int idMedico) throws SQLException {
        if (idMedico <= 0) {
            return null;
        }
        return medicoDAO.obtenerPorId(idMedico);
    }

    public void guardar(Medico medico, String usuarioRegistro) throws SQLException {
        validarMedico(medico);
        medicoDAO.guardar(medico, usuarioRegistro == null || usuarioRegistro.isBlank() ? "sistema" : usuarioRegistro);
    }

    public void eliminar(int idMedico) throws SQLException {
        if (idMedico <= 0) {
            throw new IllegalArgumentException("El id del medico no es valido.");
        }
        medicoDAO.eliminar(idMedico);
    }

    private void validarMedico(Medico medico) {
        if (medico == null) {
            throw new IllegalArgumentException("No se recibio informacion del medico.");
        }

        if (isBlank(medico.getNombres()) || isBlank(medico.getApellidos()) || medico.getIdEspecialidad() <= 0) {
            throw new IllegalArgumentException("Nombres, apellidos y especialidad son obligatorios.");
        }
        if (medico.getDuracionCitaMinutos() <= 0) medico.setDuracionCitaMinutos(30);
        if (medico.getToleranciaMinutos() < 0) medico.setToleranciaMinutos(10);
        if (medico.getMaxCitasDia() <= 0) medico.setMaxCitasDia(12);
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
