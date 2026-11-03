package pe.edu.utp.farmacia.dao;

import pe.edu.utp.farmacia.model.UsuarioFarmacia;
import java.sql.SQLException;
import java.util.List;

public interface UsuarioFarmaciaDAO {
    List<UsuarioFarmacia> listar() throws SQLException;
    UsuarioFarmacia obtenerPorId(int id) throws SQLException;
    UsuarioFarmacia obtenerPorUsername(String username) throws SQLException;
    int registrar(UsuarioFarmacia u, String usuarioRegistro) throws SQLException;
    void actualizar(UsuarioFarmacia u, String usuarioActualizacion) throws SQLException;
    void eliminar(int id, String usuarioActualizacion) throws SQLException;
}
