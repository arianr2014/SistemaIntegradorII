package pe.edu.utp.farmacia.dao;

import pe.edu.utp.farmacia.model.Venta;
import pe.edu.utp.farmacia.model.VentaDetalle;
import java.sql.SQLException;
import java.util.List;

public interface VentaDAO {
    List<Venta> listar() throws SQLException;
    Venta obtenerPorId(int id) throws SQLException;
    int registrar(Venta v, String usuarioRegistro) throws SQLException;
    void actualizar(Venta v, String usuarioActualizacion) throws SQLException;
    void eliminar(int id, String usuarioActualizacion) throws SQLException;

    // Métodos de Detalle de Venta
    List<VentaDetalle> listarDetalles(int idVenta) throws SQLException;
    int registrarDetalle(VentaDetalle d, String usuarioRegistro) throws SQLException;
}
