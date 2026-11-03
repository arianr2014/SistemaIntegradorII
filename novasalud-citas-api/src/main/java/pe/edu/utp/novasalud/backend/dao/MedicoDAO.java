package pe.edu.utp.novasalud.backend.dao;

import pe.edu.utp.novasalud.backend.model.Medico;
import java.sql.SQLException;
import java.util.List;

/** DAO de médicos con parámetros de agenda y capacidad diaria. */
public interface MedicoDAO {

    List<Medico> listar(String filtro) throws SQLException;

    Medico obtenerPorId(int idMedico) throws SQLException;

    List<Medico> listarPorEspecialidad(int idEspecialidad) throws SQLException;

    void guardar(Medico medico, String usuarioRegistro) throws SQLException;

    void eliminar(int idMedico) throws SQLException;

    int contarCitasActivasDia(int idMedico, String fecha) throws SQLException;
}
