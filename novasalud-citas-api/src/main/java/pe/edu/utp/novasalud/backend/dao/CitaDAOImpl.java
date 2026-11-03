package pe.edu.utp.novasalud.backend.dao;

import pe.edu.utp.novasalud.backend.model.Cita;
import pe.edu.utp.novasalud.backend.util.ConexionDB;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CitaDAOImpl implements CitaDAO {

    private static final DateTimeFormatter HORA_MINUTOS = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public Map<String, Integer> contarCitasHoyPorEstado(String fechaHoy) throws SQLException {
        Map<String, Integer> resultado = new LinkedHashMap<>();
        String sql = "SELECT UPPER(estado) AS estado, COUNT(*) AS total "
                + "FROM cita "
                + "WHERE estado_registro = 'ACTIVO' AND fecha = ? "
                + "GROUP BY UPPER(estado) "
                + "ORDER BY total DESC";

        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, fechaHoy);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    resultado.put(rs.getString("estado"), rs.getInt("total"));
                }
            }
        }

        return resultado;
    }

    @Override
    public int contarMedicosEnTurnoHoy(String fechaHoy) throws SQLException {
        String sql = "SELECT COUNT(DISTINCT id_medico) AS total "
                + "FROM cita "
                + "WHERE estado_registro = 'ACTIVO' AND fecha = ? AND UPPER(estado) <> 'CANCELADA'";

        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, fechaHoy);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        }

        return 0;
    }

    @Override
    public Map<String, Integer> topEspecialidadesPorRango(String fechaInicio, String fechaFin) throws SQLException {
        Map<String, Integer> resultado = new LinkedHashMap<>();
        String sql = "SELECT e.nombre AS especialidad, COUNT(*) AS total "
                + "FROM cita c "
                + "INNER JOIN medico m ON c.id_medico = m.id_medico "
                + "INNER JOIN especialidad e ON m.id_especialidad = e.id_especialidad "
                + "WHERE c.estado_registro = 'ACTIVO' AND c.fecha BETWEEN ? AND ? "
                + "GROUP BY e.id_especialidad, e.nombre "
                + "ORDER BY total DESC, e.nombre ASC";

        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, fechaInicio);
            statement.setString(2, fechaFin);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    resultado.put(rs.getString("especialidad"), rs.getInt("total"));
                }
            }
        }

        return resultado;
    }

    @Override
    public Map<String, Integer> resumenSemanalPorDia(String fechaInicio, String fechaFin) throws SQLException {
        Map<String, Integer> resultado = new LinkedHashMap<>();
        String sql = "SELECT fecha, COUNT(*) AS total "
                + "FROM cita "
                + "WHERE estado_registro = 'ACTIVO' AND fecha BETWEEN ? AND ? "
                + "GROUP BY fecha "
                + "ORDER BY fecha ASC";

        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, fechaInicio);
            statement.setString(2, fechaFin);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    resultado.put(rs.getString("fecha"), rs.getInt("total"));
                }
            }
        }

        return resultado;
    }

    @Override
    public List<Cita> listarFiltrado(String dniPaciente, int idMedico, int idEspecialidad) throws SQLException {
        List<Cita> citas = new ArrayList<>();
        String dni = dniPaciente == null ? "" : dniPaciente.trim();

        StringBuilder sql = new StringBuilder(
                "SELECT c.id_cita, c.id_paciente, c.id_medico, c.fecha, c.hora, c.estado, c.observaciones, "
                + "p.dni, p.nombres AS paciente_nombres, p.apellidos AS paciente_apellidos, "
                + "m.nombres AS medico_nombres, m.apellidos AS medico_apellidos, e.nombre AS especialidad, "
                + "COALESCE(pg.estado_pago, 'PENDIENTE') AS estado_pago, pg.id_pago, a.id_atencion AS id_atencion "
                + "FROM cita c "
                + "INNER JOIN paciente p ON c.id_paciente = p.id_paciente "
                + "INNER JOIN medico m ON c.id_medico = m.id_medico "
                + "INNER JOIN especialidad e ON m.id_especialidad = e.id_especialidad "
                + "LEFT JOIN pago pg ON c.id_cita = pg.id_cita AND pg.estado_registro = 'ACTIVO' "
                + "LEFT JOIN atencion_medica a ON c.id_cita = a.id_cita AND a.estado_registro = 'ACTIVO' "
                + "WHERE c.estado_registro = 'ACTIVO' "
        );

        if (!dni.isEmpty()) {
            sql.append("AND p.dni = ? ");
        }

        if (idMedico > 0) {
            sql.append("AND c.id_medico = ? ");
        }

        if (idEspecialidad > 0) {
            sql.append("AND m.id_especialidad = ? ");
        }

        sql.append("ORDER BY c.fecha DESC, c.hora DESC");

        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql.toString())) {

            int index = 1;
            if (!dni.isEmpty()) {
                statement.setString(index++, dni);
            }
            if (idMedico > 0) {
                statement.setInt(index++, idMedico);
            }
            if (idEspecialidad > 0) {
                statement.setInt(index, idEspecialidad);
            }

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    citas.add(mapperListado(rs));
                }
            }
        }

        return citas;
    }

    @Override
    public List<Cita> listarAgendaProgramada(int idMedico, String fecha) throws SQLException {
        List<Cita> citas = new ArrayList<>();
        String sql = "SELECT c.id_cita, c.id_paciente, c.id_medico, c.fecha, c.hora, c.estado, c.observaciones, "
                + "p.dni, p.nombres AS paciente_nombres, p.apellidos AS paciente_apellidos, "
                + "m.nombres AS medico_nombres, m.apellidos AS medico_apellidos, e.nombre AS especialidad, "
                + "COALESCE(pg.estado_pago, 'PENDIENTE') AS estado_pago, pg.id_pago, a.id_atencion AS id_atencion "
                + "FROM cita c "
                + "INNER JOIN paciente p ON c.id_paciente = p.id_paciente "
                + "INNER JOIN medico m ON c.id_medico = m.id_medico "
                + "INNER JOIN especialidad e ON m.id_especialidad = e.id_especialidad "
                + "LEFT JOIN pago pg ON c.id_cita = pg.id_cita AND pg.estado_registro = 'ACTIVO' "
                + "LEFT JOIN atencion_medica a ON c.id_cita = a.id_cita AND a.estado_registro = 'ACTIVO' "
                + "WHERE c.estado_registro = 'ACTIVO' "
                + "AND UPPER(COALESCE(c.estado, '')) = 'PROGRAMADA' "
                + "AND c.id_medico = ? "
                + "AND c.fecha = ? "
                + "ORDER BY c.hora ASC";

        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idMedico);
            statement.setString(2, fecha);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    citas.add(mapperListado(rs));
                }
            }
        }

        return citas;
    }

    @Override
    public Cita obtenerPorId(int idCita) throws SQLException {
        String sql = "SELECT c.id_cita, c.id_paciente, c.id_medico, c.id_especialidad, c.id_tarifa_aplicada, c.id_usuario_recepcionista, "
                + "c.monto_consulta, c.fecha, c.hora, c.duracion_minutos, c.estado, c.observaciones, "
                + "p.dni AS paciente_dni, COALESCE(p.historia_clinica_codigo, CONCAT('HC-', p.dni)) AS historia_clinica_codigo, "
                + "CONCAT(p.nombres, ' ', p.apellidos) AS paciente_nombres, "
                + "CONCAT(m.nombres, ' ', m.apellidos) AS medico_nombres, "
                + "e.nombre AS especialidad, "
                + "COALESCE(pg.estado_pago, 'PENDIENTE') AS estado_pago, pg.id_pago, a.id_atencion AS id_atencion "
                + "FROM cita c "
                + "INNER JOIN paciente p ON c.id_paciente = p.id_paciente "
                + "INNER JOIN medico m ON c.id_medico = m.id_medico "
                + "INNER JOIN especialidad e ON m.id_especialidad = e.id_especialidad "
                + "LEFT JOIN pago pg ON c.id_cita = pg.id_cita AND pg.estado_registro = 'ACTIVO' "
                + "LEFT JOIN atencion_medica a ON c.id_cita = a.id_cita AND a.estado_registro = 'ACTIVO' "
                + "WHERE c.id_cita = ? AND c.estado_registro = 'ACTIVO'";

        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idCita);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    Cita cita = new Cita();
                    cita.setIdCita(rs.getInt("id_cita"));
                    cita.setIdPaciente(rs.getInt("id_paciente"));
                    cita.setIdMedico(rs.getInt("id_medico"));
                    cita.setIdEspecialidad((Integer) rs.getObject("id_especialidad"));
                    cita.setIdTarifaAplicada((Integer) rs.getObject("id_tarifa_aplicada"));
                    cita.setIdUsuarioRecepcionista((Integer) rs.getObject("id_usuario_recepcionista"));
                    cita.setMontoConsultaDecimal(rs.getBigDecimal("monto_consulta"));
                    cita.setFecha(rs.getString("fecha"));
                    cita.setHora(formatHora(rs.getTime("hora")));
                    cita.setDuracionMinutos((Integer) rs.getObject("duracion_minutos"));
                    cita.setEstado(rs.getString("estado"));
                    cita.setObservaciones(rs.getString("observaciones"));
                    cita.setPacienteDni(rs.getString("paciente_dni"));
                    cita.setHistoriaClinicaCodigo(rs.getString("historia_clinica_codigo"));
                    cita.setPacienteNombreCompleto(rs.getString("paciente_nombres"));
                    cita.setMedicoNombreCompleto(rs.getString("medico_nombres"));
                    cita.setEspecialidad(rs.getString("especialidad"));
                    cita.setEstadoPago(rs.getString("estado_pago"));
                    cita.setIdPago((Integer) rs.getObject("id_pago"));
                    cita.setIdAtencion((Integer) rs.getObject("id_atencion"));
                    return cita;
                }
            }
        }

        return null;
    }

    @Override
    public void guardar(Cita cita, String usuarioRegistro) throws SQLException {
        if (cita.getIdCita() > 0) {
            actualizar(cita);
        } else {
            registrar(cita, usuarioRegistro);
        }
    }

    private void registrar(Cita cita, String usuarioRegistro) throws SQLException {
        String sp = "{CALL sp_registrar_cita(?, ?, ?, ?, ?, ?, ?)}";

        try (Connection connection = ConexionDB.getConnection();
             CallableStatement statement = connection.prepareCall(sp)) {
            statement.setInt(1, cita.getIdPaciente());
            statement.setInt(2, cita.getIdMedico());
            statement.setString(3, cita.getFecha());
            statement.setString(4, cita.getHora());
            statement.setString(5, cita.getEstado());
            statement.setString(6, cita.getObservaciones());
            statement.setString(7, usuarioRegistro);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    cita.setIdCita(rs.getInt(1));
                }
            }
        }
    }

    private void actualizar(Cita cita) throws SQLException {
        String sp = "{CALL sp_actualizar_cita(?, ?, ?, ?, ?, ?, ?)}";

        try (Connection connection = ConexionDB.getConnection();
             CallableStatement statement = connection.prepareCall(sp)) {
            statement.setInt(1, cita.getIdCita());
            statement.setInt(2, cita.getIdPaciente());
            statement.setInt(3, cita.getIdMedico());
            statement.setString(4, cita.getFecha());
            statement.setString(5, cita.getHora());
            statement.setString(6, cita.getEstado());
            statement.setString(7, cita.getObservaciones());
            statement.executeUpdate();
        }
    }

    @Override
    public void eliminar(int idCita) throws SQLException {
        String sp = "{CALL sp_eliminar_cita(?)}";

        try (Connection connection = ConexionDB.getConnection();
             CallableStatement statement = connection.prepareCall(sp)) {
            statement.setInt(1, idCita);
            statement.executeUpdate();
        } catch (SQLException ex) {
            eliminarLogicoDirecto(idCita);
        }
    }

    private void eliminarLogicoDirecto(int idCita) throws SQLException {
        String sql = "UPDATE cita SET estado_registro = 'INACTIVO' WHERE id_cita = ?";

        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idCita);
            statement.executeUpdate();
        }
    }

    @Override
    public boolean actualizarEstado(int idCita, String estado) throws SQLException {
        String sql = "UPDATE cita "
                + "SET estado = ? "
                + "WHERE id_cita = ? AND estado_registro = 'ACTIVO' "
                + "AND UPPER(COALESCE(estado, '')) NOT IN ('ATENDIDA', 'CANCELADA')";

        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, estado == null ? "" : estado.trim().toUpperCase());
            statement.setInt(2, idCita);
            return statement.executeUpdate() > 0;
        }
    }

    private Cita mapperListado(ResultSet rs) throws SQLException {
        Cita cita = new Cita();
        cita.setIdCita(rs.getInt("id_cita"));
        cita.setIdPaciente(rs.getInt("id_paciente"));
        cita.setIdMedico(rs.getInt("id_medico"));
        cita.setFecha(rs.getString("fecha"));
        cita.setHora(formatHora(rs.getTime("hora")));
        cita.setEstado(rs.getString("estado"));
        cita.setObservaciones(rs.getString("observaciones"));
        cita.setPacienteDni(rs.getString("dni"));
        cita.setPacienteNombreCompleto((rs.getString("paciente_nombres") + " " + rs.getString("paciente_apellidos")).trim());
        cita.setMedicoNombreCompleto((rs.getString("medico_nombres") + " " + rs.getString("medico_apellidos")).trim());
        cita.setEspecialidad(rs.getString("especialidad"));

        try {
            String ep = rs.getString("estado_pago");
            cita.setEstadoPago(ep == null || ep.isBlank() ? "PENDIENTE" : ep);
        } catch (SQLException ignored) {
            cita.setEstadoPago("PENDIENTE");
        }

        try {
            cita.setIdPago((Integer) rs.getObject("id_pago"));
        } catch (SQLException ignored) {}

        try {
            cita.setIdAtencion((Integer) rs.getObject("id_atencion"));
        } catch (SQLException ignored) {}

        return cita;
    }

    private String formatHora(Time time) {
        return time == null ? "" : time.toLocalTime().format(HORA_MINUTOS);
    }

    @Override
    public List<Cita> listarAgendaProgramadaFlexible(int idMedico, int idEspecialidad, String fecha) throws SQLException {
        List<Cita> citas = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT c.id_cita, c.id_paciente, c.id_medico, c.fecha, c.hora, c.estado, c.observaciones, "
                + "p.dni, p.nombres AS paciente_nombres, p.apellidos AS paciente_apellidos, "
                + "m.nombres AS medico_nombres, m.apellidos AS medico_apellidos, e.nombre AS especialidad, "
                + "COALESCE(pg.estado_pago, 'PENDIENTE') AS estado_pago, pg.id_pago, a.id_atencion AS id_atencion "
                + "FROM cita c "
                + "INNER JOIN paciente p ON c.id_paciente = p.id_paciente "
                + "INNER JOIN medico m ON c.id_medico = m.id_medico "
                + "INNER JOIN especialidad e ON m.id_especialidad = e.id_especialidad "
                + "LEFT JOIN pago pg ON c.id_cita = pg.id_cita AND pg.estado_registro = 'ACTIVO' "
                + "LEFT JOIN atencion_medica a ON c.id_cita = a.id_cita AND a.estado_registro = 'ACTIVO' "
                + "WHERE c.estado_registro = 'ACTIVO' AND c.fecha = ? "
                + "AND UPPER(COALESCE(c.estado,'')) IN ('PROGRAMADA','PAGADO','CONFIRMADA','REPROGRAMADA','EN_ESPERA','EN_CONSULTA','ATENDIDA','NO_ASISTIO') "
        );
        if (idMedico > 0) sql.append("AND c.id_medico = ? ");
        if (idEspecialidad > 0) sql.append("AND m.id_especialidad = ? ");
        sql.append("ORDER BY m.apellidos ASC, m.nombres ASC, c.hora ASC");
        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            int i = 1;
            statement.setString(i++, fecha);
            if (idMedico > 0) statement.setInt(i++, idMedico);
            if (idEspecialidad > 0) statement.setInt(i, idEspecialidad);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) citas.add(mapperListado(rs));
            }
        }
        return citas;
    }

    @Override
    public Map<String, Integer> resumenMensualPorMedico(int idMedico, String inicioMes, String finMes) throws SQLException {
        Map<String, Integer> resultado = new LinkedHashMap<>();
        String sql = "SELECT fecha, COUNT(*) AS total FROM cita WHERE estado_registro='ACTIVO' AND id_medico=? AND fecha BETWEEN ? AND ? GROUP BY fecha ORDER BY fecha";
        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idMedico);
            statement.setString(2, inicioMes);
            statement.setString(3, finMes);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) resultado.put(rs.getString("fecha"), rs.getInt("total"));
            }
        }
        return resultado;
    }

    @Override
    public int contarCitasActivasMedicoFecha(int idMedico, String fecha, int idCitaExcluir) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM cita WHERE id_medico=? AND fecha=? AND estado_registro='ACTIVO' AND UPPER(estado) NOT IN ('CANCELADA','ANULADA','NO_ASISTIO') ");
        if (idCitaExcluir > 0) sql.append("AND id_cita<>? ");
        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            statement.setInt(1, idMedico);
            statement.setString(2, fecha);
            if (idCitaExcluir > 0) statement.setInt(3, idCitaExcluir);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    @Override
    public boolean actualizarEstadoPorMedico(int idCita, int idMedico, String estado) throws SQLException {
        try (Connection connection = ConexionDB.getConnection()) {
            return actualizarEstadoPorMedicoConConexion(connection, idCita, idMedico, estado);
        }
    }

    @Override
    public boolean actualizarEstadoPorMedicoConConexion(Connection connection, int idCita, int idMedico, String estado) throws SQLException {
        String sql = "UPDATE cita SET estado=?, fecha_actualizacion=NOW() WHERE id_cita=? AND id_medico=? AND estado_registro='ACTIVO' AND UPPER(COALESCE(estado,'')) NOT IN ('ATENDIDA','ANULADA','CANCELADA','NO_ASISTIO')";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, estado == null ? "" : estado.trim().toUpperCase());
            statement.setInt(2, idCita);
            statement.setInt(3, idMedico);
            return statement.executeUpdate() > 0;
        }
    }
}
