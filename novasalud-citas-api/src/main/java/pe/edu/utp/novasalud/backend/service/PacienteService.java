package pe.edu.utp.novasalud.backend.service;

import pe.edu.utp.novasalud.backend.dao.PacienteDAO;
import pe.edu.utp.novasalud.backend.dao.PacienteDAOImpl;
import pe.edu.utp.novasalud.backend.model.Paciente;
import java.sql.SQLException;
import java.util.List;

public class PacienteService {

    private final PacienteDAO pacienteDAO = new PacienteDAOImpl();

    public List<Paciente> listar(String filtro) throws SQLException {
        return pacienteDAO.listar(filtro);
    }

    public Paciente obtenerPorId(int idPaciente) throws SQLException {
        if (idPaciente <= 0) {
            return null;
        }
        return pacienteDAO.obtenerPorId(idPaciente);
    }

    public Paciente obtenerPorDni(String dni) throws SQLException {
        if (dni == null || dni.isBlank()) {
            return null;
        }
        return pacienteDAO.obtenerPorDni(dni.trim());
    }

    public void guardar(Paciente paciente, String usuarioRegistro) throws SQLException {
        validarPaciente(paciente);
        pacienteDAO.guardar(paciente, usuarioRegistro == null || usuarioRegistro.isBlank() ? "sistema" : usuarioRegistro);
    }

    public void eliminar(int idPaciente) throws SQLException {
        if (idPaciente <= 0) {
            throw new IllegalArgumentException("El id del paciente no es valido.");
        }
        pacienteDAO.eliminar(idPaciente);
    }

    private void validarPaciente(Paciente paciente) {
        if (paciente == null) {
            throw new IllegalArgumentException("No se recibio informacion del paciente.");
        }

        if (isBlank(paciente.getDni()) || isBlank(paciente.getNombres()) || isBlank(paciente.getApellidos())) {
            throw new IllegalArgumentException("DNI, nombres y apellidos son obligatorios.");
        }

        if (paciente.getDni().length() < 8) {
            throw new IllegalArgumentException("El DNI debe tener al menos 8 caracteres.");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
