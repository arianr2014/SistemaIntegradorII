package pe.edu.utp.novasalud.backend.dao;

import pe.edu.utp.novasalud.backend.model.Paciente;
import pe.edu.utp.novasalud.backend.util.ConexionDB;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAOImpl implements PacienteDAO {

    @Override
    public List<Paciente> listar(String filtro) throws SQLException {
        List<Paciente> pacientes = new ArrayList<>();
        String texto = filtro == null ? "" : filtro.trim();

        StringBuilder sql = new StringBuilder(
        "SELECT id_paciente, dni, nombres, apellidos, telefono, direccion "
        + "FROM paciente "
        + "WHERE estado_registro = 'ACTIVO' "
        );

        if (!texto.isEmpty()) {
            sql.append("AND (dni LIKE ? OR nombres LIKE ? OR apellidos LIKE ?) ");
        }

        sql.append("ORDER BY id_paciente DESC");

        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql.toString())) {

            if (!texto.isEmpty()) {
                String like = "%" + texto + "%";
                statement.setString(1, like);
                statement.setString(2, like);
                statement.setString(3, like);
            }

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Paciente paciente = new Paciente();
                    paciente.setIdPaciente(rs.getInt("id_paciente"));
                    paciente.setDni(rs.getString("dni"));
                    paciente.setNombres(rs.getString("nombres"));
                    paciente.setApellidos(rs.getString("apellidos"));
                    paciente.setTelefono(rs.getString("telefono"));
                    paciente.setDireccion(rs.getString("direccion"));
                    pacientes.add(paciente);
                }
            }
        }

        return pacientes;
    }

    @Override
    public Paciente obtenerPorId(int idPaciente) throws SQLException {
        String sql = "SELECT id_paciente, dni, nombres, apellidos, telefono, direccion "
        + "FROM paciente "
        + "WHERE id_paciente = ? AND estado_registro = 'ACTIVO'";

        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idPaciente);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new Paciente(
                            rs.getInt("id_paciente"),
                            rs.getString("dni"),
                            rs.getString("nombres"),
                            rs.getString("apellidos"),
                            rs.getString("telefono"),
                            rs.getString("direccion")
                    );
                }
            }
        }

        return null;
    }

    @Override
    public Paciente obtenerPorDni(String dni) throws SQLException {
        String sql = "SELECT id_paciente, dni, nombres, apellidos, telefono, direccion "
        + "FROM paciente "
        + "WHERE dni = ? AND estado_registro = 'ACTIVO'";

        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, dni);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new Paciente(
                            rs.getInt("id_paciente"),
                            rs.getString("dni"),
                            rs.getString("nombres"),
                            rs.getString("apellidos"),
                            rs.getString("telefono"),
                            rs.getString("direccion")
                    );
                }
            }
        }

        return null;
    }

    @Override
    public void guardar(Paciente paciente, String usuarioRegistro) throws SQLException {
        if (paciente.getIdPaciente() > 0) {
            actualizar(paciente);
        } else {
            registrar(paciente, usuarioRegistro);
        }
    }

    private void registrar(Paciente paciente, String usuarioRegistro) throws SQLException {
        String sp = "{CALL sp_registrar_paciente(?, ?, ?, ?, ?, ?)}";

        try (Connection connection = ConexionDB.getConnection();
             CallableStatement statement = connection.prepareCall(sp)) {
            statement.setString(1, paciente.getDni());
            statement.setString(2, paciente.getNombres());
            statement.setString(3, paciente.getApellidos());
            statement.setString(4, paciente.getTelefono());
            statement.setString(5, paciente.getDireccion());
            statement.setString(6, usuarioRegistro);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    paciente.setIdPaciente(rs.getInt(1));
                }
            }
        }
    }

    private void actualizar(Paciente paciente) throws SQLException {
        String sp = "{CALL sp_actualizar_paciente(?, ?, ?, ?, ?, ?)}";

        try (Connection connection = ConexionDB.getConnection();
             CallableStatement statement = connection.prepareCall(sp)) {
            statement.setInt(1, paciente.getIdPaciente());
            statement.setString(2, paciente.getDni());
            statement.setString(3, paciente.getNombres());
            statement.setString(4, paciente.getApellidos());
            statement.setString(5, paciente.getTelefono());
            statement.setString(6, paciente.getDireccion());
            statement.executeUpdate();
        }
    }

    @Override
    public void eliminar(int idPaciente) throws SQLException {
        String sp = "{CALL sp_eliminar_paciente(?)}";

        try (Connection connection = ConexionDB.getConnection();
             CallableStatement statement = connection.prepareCall(sp)) {
            statement.setInt(1, idPaciente);
            statement.executeUpdate();
        }
    }
}
