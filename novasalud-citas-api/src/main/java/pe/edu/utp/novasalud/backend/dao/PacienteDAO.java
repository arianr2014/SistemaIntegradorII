package pe.edu.utp.novasalud.backend.dao;

import pe.edu.utp.novasalud.backend.model.Paciente;
import java.sql.SQLException;
import java.util.List;

/** DAO de pacientes con datos administrativos e historia clínica base. */
public interface PacienteDAO {

    List<Paciente> listar(String filtro) throws SQLException;

    Paciente obtenerPorId(int idPaciente) throws SQLException;

    Paciente obtenerPorDni(String dni) throws SQLException;

    void guardar(Paciente paciente, String usuarioRegistro) throws SQLException;

    void eliminar(int idPaciente) throws SQLException;
}
