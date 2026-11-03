package pe.edu.utp.novasalud.backend.service;

import pe.edu.utp.novasalud.backend.dao.EspecialidadDAO;
import pe.edu.utp.novasalud.backend.dao.EspecialidadDAOImpl;
import pe.edu.utp.novasalud.backend.model.Especialidad;
import java.sql.SQLException;
import java.util.List;

public class EspecialidadService {

    private final EspecialidadDAO especialidadDAO = new EspecialidadDAOImpl();

    public List<Especialidad> listar(String filtro) throws SQLException {
        return especialidadDAO.listar(filtro);
    }

    public Especialidad obtenerPorId(int idEspecialidad) throws SQLException {
        if (idEspecialidad <= 0) {
            return null;
        }
        return especialidadDAO.obtenerPorId(idEspecialidad);
    }

    public void guardar(Especialidad especialidad, String usuarioRegistro) throws SQLException {
        validarEspecialidad(especialidad);
        especialidadDAO.guardar(especialidad, usuarioRegistro == null || usuarioRegistro.isBlank() ? "sistema" : usuarioRegistro);
    }

    public void eliminar(int idEspecialidad) throws SQLException {
        if (idEspecialidad <= 0) {
            throw new IllegalArgumentException("El id de la especialidad no es valido.");
        }
        especialidadDAO.eliminar(idEspecialidad);
    }

    private void validarEspecialidad(Especialidad especialidad) {
        if (especialidad == null) {
            throw new IllegalArgumentException("No se recibio informacion de la especialidad.");
        }

        String nombre = especialidad.getNombre() == null ? "" : especialidad.getNombre().trim();
        String descripcion = especialidad.getDescripcion() == null ? "" : especialidad.getDescripcion().trim();

        if (nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de la especialidad es obligatorio.");
        }

        if (nombre.length() > 100) {
            throw new IllegalArgumentException("El nombre no puede superar los 100 caracteres.");
        }

        if (descripcion.length() > 255) {
            throw new IllegalArgumentException("La descripcion no puede superar los 255 caracteres.");
        }

        especialidad.setNombre(nombre);
        especialidad.setDescripcion(descripcion);
    }
}
