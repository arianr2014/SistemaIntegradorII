package pe.edu.utp.farmacia.service;

import pe.edu.utp.farmacia.dao.InventarioDAO;
import pe.edu.utp.farmacia.dao.InventarioDAOImpl;
import pe.edu.utp.farmacia.model.Inventario;
import java.sql.SQLException;
import java.util.List;

public class InventarioService {

    private final InventarioDAO dao;

    public InventarioService() {
        this.dao = new InventarioDAOImpl();
    }

    public List<Inventario> listar() throws SQLException {
        return dao.listar();
    }

    public Inventario obtenerPorId(int id) throws SQLException {
        return dao.obtenerPorId(id);
    }

    public int registrar(Inventario inv, String usuario) throws SQLException {
        if (inv.getIdMedicamento() <= 0) {
            throw new IllegalArgumentException("ID de medicamento inválido para inventario.");
        }
        return dao.registrar(inv, usuario);
    }

    public void actualizar(Inventario inv, String usuario) throws SQLException {
        if (inv.getIdInventario() <= 0) {
            throw new IllegalArgumentException("ID de inventario inválido.");
        }
        dao.actualizar(inv, usuario);
    }

    public void eliminar(int id, String usuario) throws SQLException {
        dao.eliminar(id, usuario);
    }
}
