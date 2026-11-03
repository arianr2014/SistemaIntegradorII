package pe.edu.utp.farmacia.service;

import pe.edu.utp.farmacia.dao.PrescripcionDAO;
import pe.edu.utp.farmacia.dao.PrescripcionDAOImpl;
import pe.edu.utp.farmacia.model.Prescripcion;
import java.sql.SQLException;
import java.util.List;

public class PrescripcionService {

    private final PrescripcionDAO dao;

    public PrescripcionService() {
        this.dao = new PrescripcionDAOImpl();
    }

    public List<Prescripcion> listar() throws SQLException {
        return dao.listar();
    }

    public Prescripcion obtenerPorId(int id) throws SQLException {
        return dao.obtenerPorId(id);
    }

    public int registrar(Prescripcion p, String usuario) throws SQLException {
        if (p.getCodigoPrescripcion() == null || p.getCodigoPrescripcion().isBlank()) {
            throw new IllegalArgumentException("El código de prescripción es obligatorio.");
        }
        return dao.registrar(p, usuario);
    }

    public void actualizar(Prescripcion p, String usuario) throws SQLException {
        if (p.getIdPrescripcion() <= 0) {
            throw new IllegalArgumentException("ID de prescripción inválido.");
        }
        dao.actualizar(p, usuario);
    }

    public void eliminar(int id, String usuario) throws SQLException {
        dao.eliminar(id, usuario);
    }
}
