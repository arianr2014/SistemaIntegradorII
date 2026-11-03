package pe.edu.utp.novasalud.backend.dao;

import pe.edu.utp.novasalud.backend.model.Especialidad;
import java.sql.SQLException;
import java.util.List;

public interface EspecialidadDAO {

    List<Especialidad> listar(String filtro) throws SQLException;

    List<Especialidad> listar() throws SQLException;

    Especialidad obtenerPorId(int idEspecialidad) throws SQLException;

    void guardar(Especialidad especialidad, String usuarioRegistro) throws SQLException;

    void eliminar(int idEspecialidad) throws SQLException;
}
