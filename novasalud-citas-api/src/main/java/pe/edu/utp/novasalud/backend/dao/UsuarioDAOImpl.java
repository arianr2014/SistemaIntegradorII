package pe.edu.utp.novasalud.backend.dao;

import pe.edu.utp.novasalud.backend.model.Usuario;
import pe.edu.utp.novasalud.backend.util.ConexionDB;
import pe.edu.utp.novasalud.backend.util.PasswordUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del DAO de usuarios del sistema.
 */
public class UsuarioDAOImpl implements UsuarioDAO {

    @Override
    public Usuario validarCredenciales(String username, String password) throws SQLException {
        String sql = "SELECT u.id_usuario, u.username, u.password, u.rol, u.dni, u.nombres, u.apellidos, u.telefono, u.correo, u.cargo, u.session_version, "
                + "COALESCE(u.estado_registro, 'ACTIVO') AS estado_registro, "
                + "m.id_medico AS id_medico_activo, "
                + "CONCAT(COALESCE(m.nombres, ''), ' ', COALESCE(m.apellidos, '')) AS medico, "
                + "e.id_especialidad, e.nombre AS especialidad "
                + "FROM usuario u "
                + "LEFT JOIN medico m ON u.id_medico = m.id_medico AND COALESCE(m.estado_registro, 'ACTIVO') = 'ACTIVO' "
                + "LEFT JOIN especialidad e ON m.id_especialidad = e.id_especialidad AND COALESCE(e.estado_registro, 'ACTIVO') = 'ACTIVO' "
                + "WHERE u.username = ? AND COALESCE(u.estado_registro, 'ACTIVO') = 'ACTIVO'";

        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet rs = statement.executeQuery()) {
                if (!rs.next()) return null;
                String passwordAlmacenado = rs.getString("password");
                if (!PasswordUtil.verificarPassword(password, passwordAlmacenado)) return null;
                Usuario usuario = mapperBase(rs);
                usuario.setPassword(null);
                return usuario;
            }
        }
    }

    @Override
    public List<Usuario> listar(String filtro) throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String texto = filtro == null ? "" : filtro.trim();
        String sql = "SELECT u.id_usuario, u.username, u.rol, u.dni, u.nombres, u.apellidos, u.telefono, u.correo, u.cargo, u.session_version, COALESCE(u.estado_registro, 'ACTIVO') AS estado_registro, "
                + "m.id_medico AS id_medico_activo, "
                + "CONCAT(COALESCE(m.nombres, ''), ' ', COALESCE(m.apellidos, '')) AS medico, "
                + "e.id_especialidad, e.nombre AS especialidad "
                + "FROM usuario u "
                + "LEFT JOIN medico m ON u.id_medico = m.id_medico "
                + "LEFT JOIN especialidad e ON m.id_especialidad = e.id_especialidad "
                + "WHERE (? = '' OR u.username LIKE ? OR u.rol LIKE ? OR CONCAT(COALESCE(m.nombres,''),' ',COALESCE(m.apellidos,'')) LIKE ? OR u.dni LIKE ? OR CONCAT(COALESCE(u.nombres,''),' ',COALESCE(u.apellidos,'')) LIKE ?) "
                + "ORDER BY u.id_usuario DESC";
        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            String like = "%" + texto + "%";
            statement.setString(1, texto); statement.setString(2, like); statement.setString(3, like);
            statement.setString(4, like); statement.setString(5, like); statement.setString(6, like);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) usuarios.add(mapperBase(rs));
            }
        }
        return usuarios;
    }

    @Override
    public Usuario obtenerPorId(int idUsuario) throws SQLException {
        String sql = "SELECT u.id_usuario, u.username, u.rol, u.dni, u.nombres, u.apellidos, u.telefono, u.correo, u.cargo, u.session_version, COALESCE(u.estado_registro, 'ACTIVO') AS estado_registro, "
                + "m.id_medico AS id_medico_activo, "
                + "CONCAT(COALESCE(m.nombres, ''), ' ', COALESCE(m.apellidos, '')) AS medico, "
                + "e.id_especialidad, e.nombre AS especialidad "
                + "FROM usuario u "
                + "LEFT JOIN medico m ON u.id_medico = m.id_medico "
                + "LEFT JOIN especialidad e ON m.id_especialidad = e.id_especialidad "
                + "WHERE u.id_usuario = ?";
        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idUsuario);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) return mapperBase(rs);
            }
        }
        return null;
    }

    @Override
    public boolean existeUsername(String username, int idExcluir) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario WHERE username = ? AND id_usuario <> ?";
        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setInt(2, idExcluir);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    @Override
    public boolean existeDoctorActivoParaMedico(int idMedico, int idUsuarioExcluir) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario WHERE rol='DOCTOR' AND estado_registro='ACTIVO' AND id_medico=? AND id_usuario<>?";
        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idMedico);
            statement.setInt(2, idUsuarioExcluir);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    @Override
    public void guardar(Usuario usuario, String usuarioAuditoria) throws SQLException {
        if (usuario.getIdUsuario() > 0) actualizar(usuario, usuarioAuditoria);
        else registrar(usuario, usuarioAuditoria);
    }

    @Override
    public int contarAdminsActivos() throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario WHERE rol='ADMIN' AND estado_registro='ACTIVO'";
        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    @Override
    public boolean esAdminActivo(int idUsuario) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario WHERE id_usuario=? AND rol='ADMIN' AND estado_registro='ACTIVO'";
        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idUsuario);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    @Override
    public void cambiarEstado(int idUsuario, String estado, String usuarioAuditoria) throws SQLException {
        String sql = "UPDATE usuario SET estado_registro = ?, usuario_actualizacion = ?, fecha_actualizacion = NOW(), session_version=session_version+1 WHERE id_usuario = ?";
        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, estado);
            statement.setString(2, usuarioAuditoria);
            statement.setInt(3, idUsuario);
            statement.executeUpdate();
        }
    }

    @Override
    public void resetearPassword(int idUsuario, String passwordNuevo, String usuarioAuditoria) throws SQLException {
        String sql = "UPDATE usuario SET password = ?, usuario_actualizacion = ?, fecha_actualizacion = NOW(), session_version=session_version+1 WHERE id_usuario = ?";
        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, PasswordUtil.generarHash(passwordNuevo));
            statement.setString(2, usuarioAuditoria);
            statement.setInt(3, idUsuario);
            statement.executeUpdate();
        }
    }

    @Override
    public boolean sesionSigueVigente(int idUsuario, int sessionVersion) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario WHERE id_usuario=? AND estado_registro='ACTIVO' AND session_version=?";
        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idUsuario);
            statement.setInt(2, sessionVersion);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    private void registrar(Usuario usuario, String usuarioAuditoria) throws SQLException {
        String sql = "INSERT INTO usuario(username, password, rol, id_medico, dni, nombres, apellidos, telefono, correo, cargo, estado_registro, usuario_registro, fecha_registro) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())";
        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, usuario.getUsername());
            statement.setString(2, PasswordUtil.generarHash(usuario.getPassword()));
            statement.setString(3, usuario.getRol());
            setNullableInt(statement, 4, usuario.getIdMedico());
            statement.setString(5, usuario.getDni());
            statement.setString(6, usuario.getNombres());
            statement.setString(7, usuario.getApellidos());
            statement.setString(8, usuario.getTelefono());
            statement.setString(9, usuario.getCorreo());
            statement.setString(10, usuario.getCargo());
            statement.setString(11, usuario.getEstadoRegistro());
            statement.setString(12, usuarioAuditoria);
            statement.executeUpdate();
        }
    }

    private void actualizar(Usuario usuario, String usuarioAuditoria) throws SQLException {
        String sql = "UPDATE usuario SET rol = ?, id_medico = ?, dni=?, nombres=?, apellidos=?, telefono=?, correo=?, cargo=?, estado_registro = ?, usuario_actualizacion = ?, fecha_actualizacion = NOW(), session_version=session_version+1 WHERE id_usuario = ?";
        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, usuario.getRol());
            setNullableInt(statement, 2, usuario.getIdMedico());
            statement.setString(3, usuario.getDni());
            statement.setString(4, usuario.getNombres());
            statement.setString(5, usuario.getApellidos());
            statement.setString(6, usuario.getTelefono());
            statement.setString(7, usuario.getCorreo());
            statement.setString(8, usuario.getCargo());
            statement.setString(9, usuario.getEstadoRegistro());
            statement.setString(10, usuarioAuditoria);
            statement.setInt(11, usuario.getIdUsuario());
            statement.executeUpdate();
        }
    }

    private Usuario mapperBase(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(rs.getInt("id_usuario"));
        usuario.setUsername(rs.getString("username"));
        usuario.setRol(rs.getString("rol"));
        usuario.setDni(rs.getString("dni"));
        usuario.setNombres(rs.getString("nombres"));
        usuario.setApellidos(rs.getString("apellidos"));
        usuario.setTelefono(rs.getString("telefono"));
        usuario.setCorreo(rs.getString("correo"));
        usuario.setCargo(rs.getString("cargo"));
        usuario.setSessionVersion(rs.getInt("session_version"));
        usuario.setEstadoRegistro(rs.getString("estado_registro"));
        int idMedico = rs.getInt("id_medico_activo");
        usuario.setIdMedico(rs.wasNull() ? null : idMedico);
        usuario.setMedicoNombreCompleto(rs.getString("medico"));
        int idEspecialidad = rs.getInt("id_especialidad");
        usuario.setIdEspecialidad(rs.wasNull() ? null : idEspecialidad);
        usuario.setEspecialidadNombre(rs.getString("especialidad"));
        return usuario;
    }

    private void setNullableInt(PreparedStatement statement, int index, Integer value) throws SQLException {
        if (value == null || value <= 0) statement.setNull(index, Types.INTEGER);
        else statement.setInt(index, value);
    }
}
