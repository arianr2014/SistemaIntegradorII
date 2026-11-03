package pe.edu.utp.novasalud.backend.service;

import pe.edu.utp.novasalud.backend.dao.UsuarioDAO;
import pe.edu.utp.novasalud.backend.dao.UsuarioDAOImpl;
import pe.edu.utp.novasalud.backend.model.Usuario;
import java.sql.SQLException;
import java.util.List;

/** Reglas de negocio del módulo Usuarios. */
public class UsuarioService {
    private final UsuarioDAO usuarioDAO = new UsuarioDAOImpl();

    public List<Usuario> listar(String filtro) throws SQLException { return usuarioDAO.listar(filtro); }
    public Usuario obtenerPorId(int id) throws SQLException { return usuarioDAO.obtenerPorId(id); }

    public void guardar(Usuario usuario, String passwordInicial, String auditoria) throws SQLException {
        validar(usuario, passwordInicial);
        if (usuarioDAO.existeUsername(usuario.getUsername(), usuario.getIdUsuario())) {
            throw new IllegalArgumentException("El nombre de usuario ya existe.");
        }

        if ("DOCTOR".equalsIgnoreCase(usuario.getRol())
                && usuario.getIdMedico() != null
                && usuarioDAO.existeDoctorActivoParaMedico(usuario.getIdMedico(), usuario.getIdUsuario())) {
            throw new IllegalArgumentException("El médico seleccionado ya tiene un usuario DOCTOR activo.");
        }

        if (usuario.getIdUsuario() == 0) {
            usuario.setPassword(passwordInicial);
        } else {
            Usuario actual = usuarioDAO.obtenerPorId(usuario.getIdUsuario());
            boolean eraAdminActivo = actual != null
                    && "ADMIN".equalsIgnoreCase(actual.getRol())
                    && "ACTIVO".equalsIgnoreCase(actual.getEstadoRegistro());
            boolean seguiraAdminActivo = "ADMIN".equalsIgnoreCase(usuario.getRol())
                    && "ACTIVO".equalsIgnoreCase(usuario.getEstadoRegistro());
            if (eraAdminActivo && !seguiraAdminActivo && usuarioDAO.contarAdminsActivos() <= 1) {
                throw new IllegalArgumentException("No se puede modificar el último administrador activo del sistema.");
            }
        }
        usuarioDAO.guardar(usuario, auditoria);
    }

    public void cambiarEstado(int idUsuario, String estado, String auditoria) throws SQLException {
        if (idUsuario <= 0) throw new IllegalArgumentException("Usuario inválido.");
        if (!"ACTIVO".equals(estado) && !"INACTIVO".equals(estado)) throw new IllegalArgumentException("Estado inválido.");
        if ("INACTIVO".equals(estado) && usuarioDAO.esAdminActivo(idUsuario) && usuarioDAO.contarAdminsActivos() <= 1) {
            throw new IllegalArgumentException("No se puede desactivar el último administrador activo del sistema.");
        }
        usuarioDAO.cambiarEstado(idUsuario, estado, auditoria);
    }

    public void resetearPassword(int idUsuario, String passwordNuevo, String auditoria) throws SQLException {
        if (idUsuario <= 0) throw new IllegalArgumentException("Usuario inválido.");
        if (passwordNuevo == null || passwordNuevo.length() < 6) throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres.");
        usuarioDAO.resetearPassword(idUsuario, passwordNuevo, auditoria);
    }

    private void validar(Usuario usuario, String passwordInicial) {
        if (usuario == null) throw new IllegalArgumentException("No se recibió información del usuario.");
        if (usuario.getUsername() == null || usuario.getUsername().isBlank()) throw new IllegalArgumentException("El username es obligatorio.");
        if (usuario.getDni() == null || usuario.getDni().isBlank()) throw new IllegalArgumentException("El DNI del personal es obligatorio.");
        if (usuario.getNombres() == null || usuario.getNombres().isBlank()) throw new IllegalArgumentException("Los nombres del personal son obligatorios.");
        if (usuario.getApellidos() == null || usuario.getApellidos().isBlank()) throw new IllegalArgumentException("Los apellidos del personal son obligatorios.");
        if (usuario.getRol() == null || usuario.getRol().isBlank()) throw new IllegalArgumentException("El rol es obligatorio.");
        String rol = usuario.getRol().trim().toUpperCase();
        if (!(rol.equals("ADMIN") || rol.equals("RECEPCIONISTA") || rol.equals("DOCTOR") || rol.equals("CAJERO") || rol.equals("DIRECCION"))) {
            throw new IllegalArgumentException("Rol no permitido.");
        }
        usuario.setRol(rol);
        if (usuario.getIdUsuario() == 0 && (passwordInicial == null || passwordInicial.length() < 6)) {
            throw new IllegalArgumentException("La contraseña inicial debe tener al menos 6 caracteres.");
        }
        if ("DOCTOR".equals(rol) && (usuario.getIdMedico() == null || usuario.getIdMedico() <= 0)) {
            throw new IllegalArgumentException("Un usuario DOCTOR debe asociarse a un médico real del sistema.");
        }
        if (!"DOCTOR".equals(rol)) {
            usuario.setIdMedico(null);
        }
        if (usuario.getEstadoRegistro() == null || usuario.getEstadoRegistro().isBlank()) {
            usuario.setEstadoRegistro("ACTIVO");
        }
    }
}
