package pe.edu.utp.farmacia.service;

import pe.edu.utp.farmacia.dao.VentaDAO;
import pe.edu.utp.farmacia.dao.VentaDAOImpl;
import pe.edu.utp.farmacia.model.Venta;
import java.sql.SQLException;
import java.util.List;

public class VentaService {

    private final VentaDAO dao;

    public VentaService() {
        this.dao = new VentaDAOImpl();
    }

    public List<Venta> listar() throws SQLException {
        return dao.listar();
    }

    public Venta obtenerPorId(int id) throws SQLException {
        return dao.obtenerPorId(id);
    }

    public int registrar(Venta v, String usuario) throws SQLException {
        if (v.getCodigoVenta() == null || v.getCodigoVenta().isBlank()) {
            throw new IllegalArgumentException("El código de venta es obligatorio.");
        }
        return dao.registrar(v, usuario);
    }

    public void actualizar(Venta v, String usuario) throws SQLException {
        if (v.getIdVenta() <= 0) {
            throw new IllegalArgumentException("ID de venta inválido.");
        }
        dao.actualizar(v, usuario);
    }

    public void eliminar(int id, String usuario) throws SQLException {
        dao.eliminar(id, usuario);
    }
}
