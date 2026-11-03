package pe.edu.utp.novasalud.backend.dao;

import pe.edu.utp.novasalud.backend.model.Horario;
import pe.edu.utp.novasalud.backend.util.ConexionDB;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class HorarioDAOImpl implements HorarioDAO {

    private static final DateTimeFormatter HORA_MINUTOS = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public List<Horario> listar(String filtro) throws SQLException {
        List<Horario> horarios = new ArrayList<>();
        String texto = filtro == null ? "" : filtro.trim();

        StringBuilder sql = new StringBuilder(
                "SELECT h.id_horario, h.id_medico, h.dia, h.hora_inicio, h.hora_fin, h.usuario_registro, h.fecha_registro, "
                + "m.nombres AS medico_nombres, m.apellidos AS medico_apellidos, e.nombre AS especialidad "
                + "FROM horario h "
                + "INNER JOIN medico m ON h.id_medico = m.id_medico "
                + "INNER JOIN especialidad e ON m.id_especialidad = e.id_especialidad "
                + "WHERE h.estado_registro = 'ACTIVO' "
                + "AND m.estado_registro = 'ACTIVO' "
                + "AND e.estado_registro = 'ACTIVO' "
        );

        if (!texto.isEmpty()) {
            sql.append("AND (h.dia LIKE ? OR m.nombres LIKE ? OR m.apellidos LIKE ? OR e.nombre LIKE ?) ");
        }

        sql.append(
                "ORDER BY h.id_medico, "
                + "FIELD(LOWER(TRIM(h.dia)), 'lunes', 'martes', 'miercoles', 'jueves', 'viernes', 'sabado', 'domingo'), "
                + "h.hora_inicio"
        );

        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql.toString())) {

            if (!texto.isEmpty()) {
                String like = "%" + texto + "%";
                statement.setString(1, like);
                statement.setString(2, like);
                statement.setString(3, like);
                statement.setString(4, like);
            }

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    horarios.add(mapperListado(rs));
                }
            }
        }

        return horarios;
    }

    @Override
    public List<Horario> listarPorMedico(int idMedico) throws SQLException {
        List<Horario> horarios = new ArrayList<>();
        String sql = "SELECT id_horario, id_medico, dia, hora_inicio, hora_fin "
        + "FROM horario "
        + "WHERE id_medico = ? AND estado_registro = 'ACTIVO' "
        + "ORDER BY FIELD(LOWER(TRIM(dia)), 'lunes', 'martes', 'miercoles', 'jueves', 'viernes', 'sabado', 'domingo'), hora_inicio";

        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idMedico);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    horarios.add(mapperBasico(rs));
                }
            }
        }

        return horarios;
    }

    @Override
    public Horario obtenerPorId(int idHorario) throws SQLException {
        String sql = "SELECT id_horario, id_medico, dia, hora_inicio, hora_fin "
        + "FROM horario "
        + "WHERE id_horario = ? AND estado_registro = 'ACTIVO'";

        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idHorario);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapperBasico(rs);
                }
            }
        }

        return null;
    }

    @Override
    public void guardar(Horario horario, String usuarioRegistro) throws SQLException {
        if (horario.getIdHorario() > 0) {
            actualizar(horario);
        } else {
            registrar(horario, usuarioRegistro);
        }
    }

    @Override
    public void eliminar(int idHorario) throws SQLException {
        String sp = "{CALL sp_eliminar_horario(?)}";

        try (Connection connection = ConexionDB.getConnection();
             CallableStatement statement = connection.prepareCall(sp)) {
            statement.setInt(1, idHorario);
            statement.executeUpdate();
        }
    }

    @Override
    public boolean existeCruceHorario(Horario horario) throws SQLException {
        StringBuilder sql = new StringBuilder(
            "SELECT COUNT(*) AS total FROM horario "
            + "WHERE id_medico = ? "
            + "AND estado_registro = 'ACTIVO' "
            + "AND LOWER(TRIM(dia)) = ? "
            + "AND (? < hora_fin AND ? > hora_inicio) "
    );

        if (horario.getIdHorario() > 0) {
            sql.append("AND id_horario <> ?");
        }

        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql.toString())) {

            statement.setInt(1, horario.getIdMedico());
            statement.setString(2, normalizarDia(horario.getDia()));
            statement.setTime(3, toSqlTime(horario.getHoraInicio()));
            statement.setTime(4, toSqlTime(horario.getHoraFin()));
            if (horario.getIdHorario() > 0) {
                statement.setInt(5, horario.getIdHorario());
            }

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() && rs.getInt("total") > 0;
            }
        }
    }

    @Override
    public List<String> listarHorasDisponibles(int idMedico, String fechaIso, int idCitaExcluir) throws SQLException {
        if (idMedico <= 0 || fechaIso == null || fechaIso.isBlank()) {
            return new ArrayList<>();
        }

        LocalDate fecha = LocalDate.parse(fechaIso);
        String diaSemana = mapearDia(fecha.getDayOfWeek());

        List<LocalTime[]> bloques = obtenerBloquesHorario(idMedico, diaSemana);
        Set<String> horasGeneradas = generarBloquesHora(bloques);
        Set<String> horasOcupadas = obtenerHorasOcupadas(idMedico, fechaIso, idCitaExcluir);

        horasGeneradas.removeAll(horasOcupadas);
        return new ArrayList<>(horasGeneradas);
    }

    private List<LocalTime[]> obtenerBloquesHorario(int idMedico, String diaSemana) throws SQLException {
        List<LocalTime[]> bloques = new ArrayList<>();
        String sql = "SELECT hora_inicio, hora_fin "
                + "FROM horario "
                + "WHERE id_medico = ? "
                + "AND estado_registro = 'ACTIVO' "
                + "AND LOWER(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(TRIM(dia), 'á','a'), 'é','e'), 'í','i'), 'ó','o'), 'ú','u')) = ? "
                + "ORDER BY hora_inicio";

        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idMedico);
            statement.setString(2, diaSemana);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    LocalTime inicio = rs.getTime("hora_inicio").toLocalTime();
                    LocalTime fin = rs.getTime("hora_fin").toLocalTime();
                    bloques.add(new LocalTime[]{inicio, fin});
                }
            }
        }

        return bloques;
    }

    private void registrar(Horario horario, String usuarioRegistro) throws SQLException {
        String sql = "INSERT INTO horario (id_medico, dia, hora_inicio, hora_fin, usuario_registro, fecha_registro) VALUES (?, ?, ?, ?, ?, NOW())";

        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, horario.getIdMedico());
            statement.setString(2, normalizarDia(horario.getDia()));
            statement.setTime(3, toSqlTime(horario.getHoraInicio()));
            statement.setTime(4, toSqlTime(horario.getHoraFin()));
            statement.setString(5, usuarioRegistro == null || usuarioRegistro.isBlank() ? "sistema" : usuarioRegistro);
            statement.executeUpdate();
        }
    }

    private void actualizar(Horario horario) throws SQLException {
        String sql = "UPDATE horario SET id_medico = ?, dia = ?, hora_inicio = ?, hora_fin = ? WHERE id_horario = ?";

        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, horario.getIdMedico());
            statement.setString(2, normalizarDia(horario.getDia()));
            statement.setTime(3, toSqlTime(horario.getHoraInicio()));
            statement.setTime(4, toSqlTime(horario.getHoraFin()));
            statement.setInt(5, horario.getIdHorario());
            statement.executeUpdate();
        }
    }

    private Set<String> generarBloquesHora(List<LocalTime[]> bloques) {
        Set<String> horas = new LinkedHashSet<>();

        for (LocalTime[] bloque : bloques) {
            LocalTime actual = bloque[0];
            LocalTime fin = bloque[1];

            while (actual.isBefore(fin)) {
                horas.add(actual.format(HORA_MINUTOS));
                actual = actual.plusMinutes(30);
            }
        }

        return horas;
    }

    private Set<String> obtenerHorasOcupadas(int idMedico, String fechaIso, int idCitaExcluir) throws SQLException {
        Set<String> horas = new HashSet<>();

        StringBuilder sql = new StringBuilder(
                "SELECT hora FROM cita "
                + "WHERE id_medico = ? AND fecha = ? "
                + "AND estado_registro = 'ACTIVO' "
                + "AND UPPER(COALESCE(estado, '')) <> 'CANCELADA' "
        );

        if (idCitaExcluir > 0) {
            sql.append("AND id_cita <> ? ");
        }

        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql.toString())) {

            statement.setInt(1, idMedico);
            statement.setString(2, fechaIso);
            if (idCitaExcluir > 0) {
                statement.setInt(3, idCitaExcluir);
            }

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    horas.add(rs.getTime("hora").toLocalTime().format(HORA_MINUTOS));
                }
            }
        }

        return horas;
    }

    private String mapearDia(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY:
                return "lunes";
            case TUESDAY:
                return "martes";
            case WEDNESDAY:
                return "miercoles";
            case THURSDAY:
                return "jueves";
            case FRIDAY:
                return "viernes";
            case SATURDAY:
                return "sabado";
            default:
                return "domingo";
        }
    }

    private Horario mapperBasico(ResultSet rs) throws SQLException {
        Horario horario = new Horario();
        horario.setIdHorario(rs.getInt("id_horario"));
        horario.setIdMedico(rs.getInt("id_medico"));
        horario.setDia(normalizarDia(rs.getString("dia")));
        horario.setHoraInicio(formatHora(rs.getTime("hora_inicio")));
        horario.setHoraFin(formatHora(rs.getTime("hora_fin")));
        return horario;
    }

    private Horario mapperListado(ResultSet rs) throws SQLException {
        Horario horario = mapperBasico(rs);
        horario.setUsuarioRegistro(rs.getString("usuario_registro"));
        horario.setFechaRegistro(rs.getString("fecha_registro"));
        String nombres = rs.getString("medico_nombres");
        String apellidos = rs.getString("medico_apellidos");
        horario.setMedicoNombreCompleto(((nombres == null ? "" : nombres) + " " + (apellidos == null ? "" : apellidos)).trim());
        horario.setEspecialidad(rs.getString("especialidad"));
        return horario;
    }

    private String formatHora(Time time) {
        return time == null ? "" : time.toLocalTime().format(HORA_MINUTOS);
    }

    private Time toSqlTime(String value) {
        LocalTime time = LocalTime.parse(value, HORA_MINUTOS);
        return Time.valueOf(time);
    }

    private String normalizarDia(String dia) {
        if (dia == null) {
            return "";
        }

        String texto = dia.trim().toLowerCase(Locale.ROOT)
                .replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ó", "o")
                .replace("ú", "u");

        return texto;
    }
}
