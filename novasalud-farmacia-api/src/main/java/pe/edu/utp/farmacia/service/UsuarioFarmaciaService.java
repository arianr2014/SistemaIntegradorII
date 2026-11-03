package pe.edu.utp.farmacia.service;

import pe.edu.utp.farmacia.dao.UsuarioFarmaciaDAO;
import pe.edu.utp.farmacia.dao.UsuarioFarmaciaDAOImpl;
import pe.edu.utp.farmacia.model.UsuarioFarmacia;
import pe.edu.utp.farmacia.util.PasswordUtil;
import java.sql.SQLException;
import java.util.List;

public class UsuarioFarmaciaService {

    private final UsuarioFarmaciaDAO dao;

    public UsuarioFarmaciaService() {
        this.dao = new UsuarioFarmaciaDAOImpl();
    }

    public List<UsuarioFarmacia> listar() throws SQLException {
        return dao.listar();
    }

    public UsuarioFarmacia obtenerPorId(int id) throws SQLException {
        return dao.obtenerPorId(id);
    }

    public UsuarioFarmacia autenticar(String username, String password) throws SQLException {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return null;
        }

        UsuarioFarmacia usuario = dao.obtenerPorUsername(username.trim());
        if (usuario != null && PasswordUtil.checkPassword(password, usuario.getPasswordHash())) {
            return usuario;
        }
        return null;
    }

    public int registrar(UsuarioFarmacia u, String rawPassword, String usuarioRegistro) throws SQLException {
        if (u.getUsername() == null || u.getUsername().isBlank()) {
            throw new IllegalArgumentException("El username es obligatorio.");
        }
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria.");
        }

        u.setPasswordHash(PasswordUtil.hashPassword(rawPassword));
        return dao.registrar(u, usuarioRegistro);
    }

    public void actualizar(UsuarioFarmacia u, String newRawPassword, String usuarioActualizacion) throws SQLException {
        if (u.getIdUsuario() <= 0) {
            throw new IllegalArgumentException("ID de usuario inválido.");
        }
        if (newRawPassword != null && !newRawPassword.isBlank()) {
            u.setPasswordHash(PasswordUtil.hashPassword(newRawPassword));
        }
        dao.actualizar(u, usuarioActualizacion);
    }

    public void eliminar(int id, String usuarioActualizacion) throws SQLException {
        dao.eliminar(id, usuarioActualizacion);
    }
}
