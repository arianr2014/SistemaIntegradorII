package pe.edu.utp.farmacia.service;

import pe.edu.utp.farmacia.dao.LoteMedicamentoDAO;
import pe.edu.utp.farmacia.dao.LoteMedicamentoDAOImpl;
import pe.edu.utp.farmacia.model.LoteMedicamento;
import java.sql.SQLException;
import java.util.List;

public class LoteMedicamentoService {

    private final LoteMedicamentoDAO dao;

    public LoteMedicamentoService() {
        this.dao = new LoteMedicamentoDAOImpl();
    }

    public List<LoteMedicamento> listar() throws SQLException {
        return dao.listar();
    }

    public LoteMedicamento obtenerPorId(int id) throws SQLException {
        return dao.obtenerPorId(id);
    }

    public int registrar(LoteMedicamento l, String usuario) throws SQLException {
        if (l.getNumeroLote() == null || l.getNumeroLote().isBlank()) {
            throw new IllegalArgumentException("El número de lote es obligatorio.");
        }
        if (l.getFechaVencimiento() == null) {
            throw new IllegalArgumentException("La fecha de vencimiento es obligatoria.");
        }
        return dao.registrar(l, usuario);
    }

    public void actualizar(LoteMedicamento l, String usuario) throws SQLException {
        if (l.getIdLote() <= 0) {
            throw new IllegalArgumentException("ID de lote inválido.");
        }
        dao.actualizar(l, usuario);
    }

    public void eliminar(int id, String usuario) throws SQLException {
        dao.eliminar(id, usuario);
    }
}
