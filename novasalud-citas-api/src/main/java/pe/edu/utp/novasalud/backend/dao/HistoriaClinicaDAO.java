package pe.edu.utp.novasalud.backend.dao;

import pe.edu.utp.novasalud.backend.model.HistoriaClinica;
import pe.edu.utp.novasalud.backend.model.Paciente;
import pe.edu.utp.novasalud.backend.util.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** DAO para la entidad `historia_clinica` y su vinculación operativa. */
public class HistoriaClinicaDAO {

    public HistoriaClinica obtenerPorIdPaciente(int idPaciente) throws SQLException {
        try (Connection cn = ConexionDB.getConnection()) {
            return obtenerPorIdPacienteConConexion(cn, idPaciente);
        }
    }

    public HistoriaClinica obtenerPorIdPacienteConConexion(Connection cn, int idPaciente) throws SQLException {
        String sql = "SELECT h.id_historia, h.id_paciente, h.codigo_historia, h.antecedentes_generales, h.alergias, h.usuario_registro, h.fecha_registro, h.estado_registro, "
                + "p.dni, p.nombres, p.apellidos, p.telefono, p.correo, p.direccion, p.fecha_nacimiento, p.sexo, p.contacto_emergencia, p.telefono_emergencia "
                + "FROM historia_clinica h "
                + "JOIN paciente p ON h.id_paciente = p.id_paciente "
                + "WHERE h.id_paciente = ? AND h.estado_registro = 'ACTIVO'";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idPaciente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapperConPaciente(rs);
                }
            }
        }
        return null;
    }

    public HistoriaClinica obtenerPorCodigo(String codigoHistoria) throws SQLException {
        String sql = "SELECT h.id_historia, h.id_paciente, h.codigo_historia, h.antecedentes_generales, h.alergias, h.usuario_registro, h.fecha_registro, h.estado_registro, "
                + "p.dni, p.nombres, p.apellidos, p.telefono, p.correo, p.direccion, p.fecha_nacimiento, p.sexo, p.contacto_emergencia, p.telefono_emergencia "
                + "FROM historia_clinica h "
                + "JOIN paciente p ON h.id_paciente = p.id_paciente "
                + "WHERE h.codigo_historia = ? AND h.estado_registro = 'ACTIVO'";
        try (Connection cn = ConexionDB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, codigoHistoria);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapperConPaciente(rs);
                }
            }
        }
        return null;
    }

    public void asegurarExisteHistoriaClinica(Connection cn, int idPaciente, String codigoHistoria, String usuarioAudit) throws SQLException {
        HistoriaClinica existente = obtenerPorIdPacienteConConexion(cn, idPaciente);
        if (existente == null) {
            String codigoUsar = (codigoHistoria != null && !codigoHistoria.isBlank()) ? codigoHistoria : String.valueOf(idPaciente);
            String sql = "INSERT INTO historia_clinica(id_paciente, codigo_historia, antecedentes_generales, alergias, usuario_registro, fecha_registro, estado_registro) "
                    + "VALUES (?, ?, 'Sin antecedentes críticos registrados.', 'No refiere alergias conocidas.', ?, NOW(), 'ACTIVO') "
                    + "ON DUPLICATE KEY UPDATE estado_registro='ACTIVO'";
            try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, idPaciente);
                ps.setString(2, codigoUsar);
                ps.setString(3, (usuarioAudit != null && !usuarioAudit.isBlank()) ? usuarioAudit : "system");
                ps.executeUpdate();
            }
        }
    }

    public boolean actualizarAntecedentesYAlergias(int idPaciente, String antecedentes, String alergias, String usuarioAudit) throws SQLException {
        String sql = "UPDATE historia_clinica SET antecedentes_generales=?, alergias=?, usuario_registro=? "
                + "WHERE id_paciente=? AND estado_registro='ACTIVO'";
        try (Connection cn = ConexionDB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, antecedentes);
            ps.setString(2, alergias);
            ps.setString(3, (usuarioAudit != null && !usuarioAudit.isBlank()) ? usuarioAudit : "system");
            ps.setInt(4, idPaciente);
            return ps.executeUpdate() > 0;
        }
    }

    public List<HistoriaClinica> listarHistorias(String filtro) throws SQLException {
        List<HistoriaClinica> lista = new ArrayList<>();
        String texto = (filtro == null) ? "" : filtro.trim();
        String sql = "SELECT h.id_historia, h.id_paciente, h.codigo_historia, h.antecedentes_generales, h.alergias, h.usuario_registro, h.fecha_registro, h.estado_registro, "
                + "p.dni, p.nombres, p.apellidos, p.telefono, p.correo, p.direccion, p.fecha_nacimiento, p.sexo, p.contacto_emergencia, p.telefono_emergencia "
                + "FROM paciente p "
                + "LEFT JOIN historia_clinica h ON p.id_paciente = h.id_paciente AND h.estado_registro='ACTIVO' "
                + "WHERE (? = '' OR p.dni LIKE ? OR h.codigo_historia LIKE ? OR CONCAT(COALESCE(p.nombres,''),' ',COALESCE(p.apellidos,'')) LIKE ?) "
                + "AND COALESCE(p.estado_registro, 'ACTIVO') = 'ACTIVO' "
                + "ORDER BY p.id_paciente DESC";
        try (Connection cn = ConexionDB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            String like = "%" + texto + "%";
            ps.setString(1, texto);
            ps.setString(2, like);
            ps.setString(3, like);
            ps.setString(4, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapperConPaciente(rs));
                }
            }
        }
        return lista;
    }

    private HistoriaClinica mapperConPaciente(ResultSet rs) throws SQLException {
        HistoriaClinica h = new HistoriaClinica();
        h.setIdHistoria(rs.getInt("id_historia"));
        h.setIdPaciente(rs.getInt("id_paciente"));
        h.setCodigoHistoria(rs.getString("codigo_historia"));
        h.setAntecedentesGenerales(rs.getString("antecedentes_generales"));
        h.setAlergias(rs.getString("alergias"));
        h.setUsuarioRegistro(rs.getString("usuario_registro"));
        h.setFechaRegistro(rs.getString("fecha_registro"));
        h.setEstadoRegistro(rs.getString("estado_registro"));

        Paciente p = new Paciente();
        p.setIdPaciente(rs.getInt("id_paciente"));
        p.setDni(rs.getString("dni"));
        p.setNombres(rs.getString("nombres"));
        p.setApellidos(rs.getString("apellidos"));
        p.setTelefono(rs.getString("telefono"));
        p.setCorreo(rs.getString("correo"));
        p.setDireccion(rs.getString("direccion"));
        p.setFechaNacimiento(rs.getString("fecha_nacimiento"));
        p.setSexo(rs.getString("sexo"));
        p.setContactoEmergencia(rs.getString("contacto_emergencia"));
        p.setTelefonoEmergencia(rs.getString("telefono_emergencia"));
        p.setHistoriaClinicaCodigo(rs.getString("codigo_historia"));

        h.setPaciente(p);
        return h;
    }
}
