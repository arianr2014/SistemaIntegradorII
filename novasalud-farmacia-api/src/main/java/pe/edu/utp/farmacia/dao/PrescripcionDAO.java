package pe.edu.utp.farmacia.dao;

import pe.edu.utp.farmacia.model.Prescripcion;
import pe.edu.utp.farmacia.model.PrescripcionDetalle;
import java.sql.SQLException;
import java.util.List;

public interface PrescripcionDAO {
    List<Prescripcion> listar() throws SQLException;
    Prescripcion obtenerPorId(int id) throws SQLException;
    int registrar(Prescripcion p, String usuarioRegistro) throws SQLException;
    void actualizar(Prescripcion p, String usuarioActualizacion) throws SQLException;
    void eliminar(int id, String usuarioActualizacion) throws SQLException;

    // Métodos de Detalle de Prescripción
    List<PrescripcionDetalle> listarDetalles(int idPrescripcion) throws SQLException;
    int registrarDetalle(PrescripcionDetalle d, String usuarioRegistro) throws SQLException;
}
