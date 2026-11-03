package pe.edu.utp.farmacia.service;

import pe.edu.utp.farmacia.dao.CategoriaMedicamentoDAO;
import pe.edu.utp.farmacia.dao.CategoriaMedicamentoDAOImpl;
import pe.edu.utp.farmacia.model.CategoriaMedicamento;
import java.sql.SQLException;
import java.util.List;

public class CategoriaMedicamentoService {

    private final CategoriaMedicamentoDAO dao;

    public CategoriaMedicamentoService() {
        this.dao = new CategoriaMedicamentoDAOImpl();
    }

    public List<CategoriaMedicamento> listar() throws SQLException {
        return dao.listar();
    }

    public CategoriaMedicamento obtenerPorId(int id) throws SQLException {
        return dao.obtenerPorId(id);
    }

    public int registrar(CategoriaMedicamento c, String usuario) throws SQLException {
        if (c.getNombre() == null || c.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio.");
        }
        return dao.registrar(c, usuario);
    }

    public void actualizar(CategoriaMedicamento c, String usuario) throws SQLException {
        if (c.getIdCategoria() <= 0) {
            throw new IllegalArgumentException("ID de categoría inválido.");
        }
        dao.actualizar(c, usuario);
    }

    public void eliminar(int id, String usuario) throws SQLException {
        dao.eliminar(id, usuario);
    }
}
