package pe.edu.utp.farmacia.service;

import pe.edu.utp.farmacia.dao.ProveedorDAO;
import pe.edu.utp.farmacia.dao.ProveedorDAOImpl;
import pe.edu.utp.farmacia.model.Proveedor;
import java.sql.SQLException;
import java.util.List;

public class ProveedorService {

    private final ProveedorDAO dao;

    public ProveedorService() {
        this.dao = new ProveedorDAOImpl();
    }

    public List<Proveedor> listar() throws SQLException {
        return dao.listar();
    }

    public Proveedor obtenerPorId(int id) throws SQLException {
        return dao.obtenerPorId(id);
    }

    public int registrar(Proveedor p, String usuario) throws SQLException {
        if (p.getRazonSocial() == null || p.getRazonSocial().isBlank()) {
            throw new IllegalArgumentException("La razón social es obligatoria.");
        }
        return dao.registrar(p, usuario);
    }

    public void actualizar(Proveedor p, String usuario) throws SQLException {
        if (p.getIdProveedor() <= 0) {
            throw new IllegalArgumentException("ID de proveedor inválido.");
        }
        dao.actualizar(p, usuario);
    }

    public void eliminar(int id, String usuario) throws SQLException {
        dao.eliminar(id, usuario);
    }
}
