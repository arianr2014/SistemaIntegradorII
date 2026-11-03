package pe.edu.utp.novasalud.backend.dao;

import pe.edu.utp.novasalud.backend.model.Especialidad;
import pe.edu.utp.novasalud.backend.util.ConexionDB;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EspecialidadDAOImpl implements EspecialidadDAO {

    @Override
    public List<Especialidad> listar(String filtro) throws SQLException {
        List<Especialidad> especialidades = new ArrayList<>();
        String texto = filtro == null ? "" : filtro.trim();

        StringBuilder sql = new StringBuilder(
        "SELECT id_especialidad, nombre, descripcion, usuario_registro, "
        + "DATE_FORMAT(fecha_registro, '%Y-%m-%d %H:%i') AS fecha_registro "
        + "FROM especialidad "
        + "WHERE estado_registro = 'ACTIVO' "
        );

        if (!texto.isEmpty()) {
            sql.append("AND (nombre LIKE ? OR descripcion LIKE ?) ");
        }

        sql.append("ORDER BY id_especialidad DESC");

        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql.toString())) {

            if (!texto.isEmpty()) {
                String like = "%" + texto + "%";
                statement.setString(1, like);
                statement.setString(2, like);
            }

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Especialidad especialidad = new Especialidad();
                    especialidad.setIdEspecialidad(rs.getInt("id_especialidad"));
                    especialidad.setNombre(rs.getString("nombre"));
                    especialidad.setDescripcion(rs.getString("descripcion"));
                    especialidad.setUsuarioRegistro(rs.getString("usuario_registro"));
                    especialidad.setFechaRegistro(rs.getString("fecha_registro"));
                    especialidades.add(especialidad);
                }
            }
        }

        return especialidades;
    }

    @Override
    public List<Especialidad> listar() throws SQLException {
        return listar("");
    }

    @Override
    public Especialidad obtenerPorId(int idEspecialidad) throws SQLException {
        String sql = "SELECT id_especialidad, nombre, descripcion, usuario_registro, "
        + "DATE_FORMAT(fecha_registro, '%Y-%m-%d %H:%i') AS fecha_registro "
        + "FROM especialidad "
        + "WHERE id_especialidad = ? AND estado_registro = 'ACTIVO'";

        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idEspecialidad);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new Especialidad(
                            rs.getInt("id_especialidad"),
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getString("usuario_registro"),
                            rs.getString("fecha_registro")
                    );
                }
            }
        }

        return null;
    }

    @Override
    public void guardar(Especialidad especialidad, String usuarioRegistro) throws SQLException {
        if (especialidad.getIdEspecialidad() > 0) {
            actualizar(especialidad);
        } else {
            registrar(especialidad, usuarioRegistro);
        }
    }

    private void registrar(Especialidad especialidad, String usuarioRegistro) throws SQLException {
        String sql = "INSERT INTO especialidad (nombre, descripcion, usuario_registro, fecha_registro) VALUES (?, ?, ?, NOW())";

        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, especialidad.getNombre());
            statement.setString(2, especialidad.getDescripcion());
            statement.setString(3, usuarioRegistro);
            statement.executeUpdate();
        }
    }

    private void actualizar(Especialidad especialidad) throws SQLException {
        String sql = "UPDATE especialidad SET nombre = ?, descripcion = ? WHERE id_especialidad = ?";

        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, especialidad.getNombre());
            statement.setString(2, especialidad.getDescripcion());
            statement.setInt(3, especialidad.getIdEspecialidad());
            statement.executeUpdate();
        }
    }

    @Override
    public void eliminar(int idEspecialidad) throws SQLException {
        String sp = "{CALL sp_eliminar_especialidad(?)}";

        try (Connection connection = ConexionDB.getConnection();
             CallableStatement statement = connection.prepareCall(sp)) {
            statement.setInt(1, idEspecialidad);
            statement.executeUpdate();
        }
    }
}
