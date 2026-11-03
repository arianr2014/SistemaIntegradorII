package pe.edu.utp.farmacia.dao;

import pe.edu.utp.farmacia.model.CategoriaMedicamento;
import java.sql.SQLException;
import java.util.List;

public interface CategoriaMedicamentoDAO {
    List<CategoriaMedicamento> listar() throws SQLException;
    CategoriaMedicamento obtenerPorId(int id) throws SQLException;
    int registrar(CategoriaMedicamento categoria, String usuarioRegistro) throws SQLException;
    void actualizar(CategoriaMedicamento categoria, String usuarioActualizacion) throws SQLException;
    void eliminar(int id, String usuarioActualizacion) throws SQLException;
}
