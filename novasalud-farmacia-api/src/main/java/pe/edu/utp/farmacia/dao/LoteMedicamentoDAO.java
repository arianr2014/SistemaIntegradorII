package pe.edu.utp.farmacia.dao;

import pe.edu.utp.farmacia.model.LoteMedicamento;
import java.sql.SQLException;
import java.util.List;

public interface LoteMedicamentoDAO {
    List<LoteMedicamento> listar() throws SQLException;
    LoteMedicamento obtenerPorId(int id) throws SQLException;
    int registrar(LoteMedicamento lote, String usuarioRegistro) throws SQLException;
    void actualizar(LoteMedicamento lote, String usuarioActualizacion) throws SQLException;
    void eliminar(int id, String usuarioActualizacion) throws SQLException;
}
