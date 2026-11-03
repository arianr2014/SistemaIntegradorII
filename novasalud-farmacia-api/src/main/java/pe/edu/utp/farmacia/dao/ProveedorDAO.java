package pe.edu.utp.farmacia.dao;

import pe.edu.utp.farmacia.model.Proveedor;
import java.sql.SQLException;
import java.util.List;

public interface ProveedorDAO {
    List<Proveedor> listar() throws SQLException;
    Proveedor obtenerPorId(int id) throws SQLException;
    int registrar(Proveedor p, String usuarioRegistro) throws SQLException;
    void actualizar(Proveedor p, String usuarioActualizacion) throws SQLException;
    void eliminar(int id, String usuarioActualizacion) throws SQLException;
}
