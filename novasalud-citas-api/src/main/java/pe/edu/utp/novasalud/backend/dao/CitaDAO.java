package pe.edu.utp.novasalud.backend.dao;

import pe.edu.utp.novasalud.backend.model.Cita;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * DAO del módulo Citas.
 */
public interface CitaDAO {

    Map<String, Integer> contarCitasHoyPorEstado(String fechaHoy) throws SQLException;

    int contarMedicosEnTurnoHoy(String fechaHoy) throws SQLException;

    Map<String, Integer> topEspecialidadesPorRango(String fechaInicio, String fechaFin) throws SQLException;

    Map<String, Integer> resumenSemanalPorDia(String fechaInicio, String fechaFin) throws SQLException;

    List<Cita> listarFiltrado(String dniPaciente, int idMedico, int idEspecialidad) throws SQLException;

    List<Cita> listarAgendaProgramada(int idMedico, String fecha) throws SQLException;

    List<Cita> listarAgendaProgramadaFlexible(int idMedico, int idEspecialidad, String fecha) throws SQLException;

    Map<String, Integer> resumenMensualPorMedico(int idMedico, String inicioMes, String finMes) throws SQLException;

    int contarCitasActivasMedicoFecha(int idMedico, String fecha, int idCitaExcluir) throws SQLException;

    Cita obtenerPorId(int idCita) throws SQLException;

    void guardar(Cita cita, String usuarioRegistro) throws SQLException;

    void eliminar(int idCita) throws SQLException;

    boolean actualizarEstado(int idCita, String estado) throws SQLException;

    boolean actualizarEstadoPorMedico(int idCita, int idMedico, String estado) throws SQLException;

    boolean actualizarEstadoPorMedicoConConexion(Connection cn, int idCita, int idMedico, String estado) throws SQLException;
}
