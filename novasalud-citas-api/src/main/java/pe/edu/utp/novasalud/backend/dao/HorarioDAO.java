package pe.edu.utp.novasalud.backend.dao;

import pe.edu.utp.novasalud.backend.model.Horario;
import java.sql.SQLException;
import java.util.List;

public interface HorarioDAO {

    List<Horario> listar(String filtro) throws SQLException;

    List<Horario> listarPorMedico(int idMedico) throws SQLException;

    Horario obtenerPorId(int idHorario) throws SQLException;

    void guardar(Horario horario, String usuarioRegistro) throws SQLException;

    void eliminar(int idHorario) throws SQLException;

    boolean existeCruceHorario(Horario horario) throws SQLException;

    List<String> listarHorasDisponibles(int idMedico, String fechaIso, int idCitaExcluir) throws SQLException;
}
