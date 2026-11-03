package pe.edu.utp.novasalud.backend.service;

import pe.edu.utp.novasalud.backend.dao.HorarioDAO;
import pe.edu.utp.novasalud.backend.dao.HorarioDAOImpl;
import pe.edu.utp.novasalud.backend.model.Horario;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class HorarioService {

    private static final DateTimeFormatter HORA_MINUTOS = DateTimeFormatter.ofPattern("HH:mm");
    private static final Set<String> DIAS_VALIDOS = new HashSet<>(Arrays.asList(
            "lunes", "martes", "miercoles", "jueves", "viernes", "sabado", "domingo"
    ));

    private final HorarioDAO horarioDAO = new HorarioDAOImpl();

    public List<Horario> listar(String filtro) throws SQLException {
        return horarioDAO.listar(filtro);
    }

    public Horario obtenerPorId(int idHorario) throws SQLException {
        if (idHorario <= 0) {
            return null;
        }
        return horarioDAO.obtenerPorId(idHorario);
    }

    public List<String> listarHorasDisponibles(int idMedico, String fecha, int idCitaExcluir) throws SQLException {
        if (idMedico <= 0 || fecha == null || fecha.isBlank()) {
            return new java.util.ArrayList<>();
        }
        return horarioDAO.listarHorasDisponibles(idMedico, fecha.trim(), idCitaExcluir);
    }

    public void guardar(Horario horario, String usuarioRegistro) throws SQLException {
        validarHorario(horario);
        if (horarioDAO.existeCruceHorario(horario)) {
            throw new IllegalArgumentException("El medico ya tiene un horario que se cruza con el rango seleccionado.");
        }
        horarioDAO.guardar(horario, usuarioRegistro == null || usuarioRegistro.isBlank() ? "sistema" : usuarioRegistro);
    }

    public void eliminar(int idHorario) throws SQLException {
        if (idHorario <= 0) {
            throw new IllegalArgumentException("El id del horario no es valido.");
        }
        horarioDAO.eliminar(idHorario);
    }

    private void validarHorario(Horario horario) {
        if (horario == null) {
            throw new IllegalArgumentException("No se recibio informacion del horario.");
        }

        if (horario.getIdMedico() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar un medico.");
        }

        String diaNormalizado = normalizarDia(horario.getDia());
        if (!DIAS_VALIDOS.contains(diaNormalizado)) {
            throw new IllegalArgumentException("El dia seleccionado no es valido.");
        }

        LocalTime horaInicio = parseHora(horario.getHoraInicio(), "La hora de inicio es obligatoria y debe tener formato HH:mm.");
        LocalTime horaFin = parseHora(horario.getHoraFin(), "La hora de fin es obligatoria y debe tener formato HH:mm.");

        if (!horaInicio.isBefore(horaFin)) {
            throw new IllegalArgumentException("La hora de inicio debe ser menor que la hora de fin.");
        }

        horario.setDia(diaNormalizado);
        horario.setHoraInicio(horaInicio.format(HORA_MINUTOS));
        horario.setHoraFin(horaFin.format(HORA_MINUTOS));
    }

    private LocalTime parseHora(String hora, String mensajeError) {
        if (hora == null || hora.isBlank()) {
            throw new IllegalArgumentException(mensajeError);
        }

        try {
            return LocalTime.parse(hora.trim(), HORA_MINUTOS);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException(mensajeError);
        }
    }

    private String normalizarDia(String dia) {
        if (dia == null) {
            return "";
        }

        return dia.trim().toLowerCase(Locale.ROOT)
                .replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ó", "o")
                .replace("ú", "u");
    }
}
