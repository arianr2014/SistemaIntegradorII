package pe.edu.utp.farmacia.dao;

import pe.edu.utp.farmacia.model.UsuarioFarmacia;
import pe.edu.utp.farmacia.util.ConexionDB;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioFarmaciaDAOImpl implements UsuarioFarmaciaDAO {

    @Override
    public List<UsuarioFarmacia> listar() throws SQLException {
        List<UsuarioFarmacia> lista = new ArrayList<>();
        String sp = "{CALL sp_listar_usuarios_farmacia()}";

        try (Connection conn = ConexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sp);
             ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearResultSet(rs));
            }
        }
        return lista;
    }

    @Override
    public UsuarioFarmacia obtenerPorId(int id) throws SQLException {
        String sp = "{CALL sp_obtener_usuario_farmacia_por_id(?)}";
        try (Connection conn = ConexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sp)) {
            cs.setInt(1, id);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSet(rs);
                }
            }
        }
        return null;
    }

    @Override
    public UsuarioFarmacia obtenerPorUsername(String username) throws SQLException {
        String sql = "SELECT id_usuario, username, nombres, apellidos, rol, password_hash, usuario_registro, fecha_registro, estado_registro "
                + "FROM usuario_farmacia WHERE username = ? AND estado_registro = 'ACTIVO'";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    UsuarioFarmacia u = mapearResultSet(rs);
                    u.setPasswordHash(rs.getString("password_hash"));
                    return u;
                }
            }
        }
        return null;
    }

    @Override
    public int registrar(UsuarioFarmacia u, String usuarioRegistro) throws SQLException {
        String sp = "{CALL sp_registrar_usuario_farmacia(?, ?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sp)) {
            cs.setString(1, u.getUsername());
            cs.setString(2, u.getNombres());
            cs.setString(3, u.getApellidos());
            cs.setString(4, u.getRol());
            cs.setString(5, u.getPasswordHash());
            cs.setString(6, usuarioRegistro);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id_usuario");
                    u.setIdUsuario(id);
                    return id;
                }
            }
        }
        return 0;
    }

    @Override
    public void actualizar(UsuarioFarmacia u, String usuarioActualizacion) throws SQLException {
        String sp = "{CALL sp_actualizar_usuario_farmacia(?, ?, ?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sp)) {
            cs.setInt(1, u.getIdUsuario());
            cs.setString(2, u.getUsername());
            cs.setString(3, u.getNombres());
            cs.setString(4, u.getApellidos());
            cs.setString(5, u.getRol());
            cs.setString(6, u.getPasswordHash());
            cs.setString(7, usuarioActualizacion);
            cs.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id, String usuarioActualizacion) throws SQLException {
        String sp = "{CALL sp_eliminar_usuario_farmacia(?, ?)}";
        try (Connection conn = ConexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sp)) {
            cs.setInt(1, id);
            cs.setString(2, usuarioActualizacion);
            cs.executeUpdate();
        }
    }

    private UsuarioFarmacia mapearResultSet(ResultSet rs) throws SQLException {
        UsuarioFarmacia u = new UsuarioFarmacia();
        u.setIdUsuario(rs.getInt("id_usuario"));
        u.setUsername(rs.getString("username"));
        u.setNombres(rs.getString("nombres"));
        u.setApellidos(rs.getString("apellidos"));
        u.setRol(rs.getString("rol"));
        u.setUsuarioRegistro(rs.getString("usuario_registro"));
        u.setFechaRegistro(rs.getTimestamp("fecha_registro"));
        u.setEstadoRegistro(rs.getString("estado_registro"));
        return u;
    }
}
