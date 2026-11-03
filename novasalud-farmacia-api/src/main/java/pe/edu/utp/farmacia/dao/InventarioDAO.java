package pe.edu.utp.farmacia.dao;

import pe.edu.utp.farmacia.model.Inventario;
import java.sql.SQLException;
import java.util.List;

public interface InventarioDAO {
    List<Inventario> listar() throws SQLException;
    Inventario obtenerPorId(int id) throws SQLException;
    int registrar(Inventario inv, String usuarioRegistro) throws SQLException;
    void actualizar(Inventario inv, String usuarioActualizacion) throws SQLException;
    void eliminar(int id, String usuarioActualizacion) throws SQLException;
}
