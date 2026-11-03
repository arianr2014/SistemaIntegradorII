package pe.edu.utp.novasalud.backend.dao;

import pe.edu.utp.novasalud.backend.model.Usuario;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO de usuarios.
 *
 * V3.2.1:
 * - Login solo con usuarios activos.
 * - Un DOCTOR debe estar vinculado a un médico activo.
 * - Se evita asignar dos usuarios DOCTOR activos al mismo médico.
 */
public interface UsuarioDAO {

    Usuario validarCredenciales(String username, String password) throws SQLException;

    List<Usuario> listar(String filtro) throws SQLException;

    Usuario obtenerPorId(int idUsuario) throws SQLException;

    boolean existeUsername(String username, int idExcluir) throws SQLException;

    boolean existeDoctorActivoParaMedico(int idMedico, int idUsuarioExcluir) throws SQLException;

    void guardar(Usuario usuario, String usuarioAuditoria) throws SQLException;

    int contarAdminsActivos() throws SQLException;

    boolean esAdminActivo(int idUsuario) throws SQLException;

    void cambiarEstado(int idUsuario, String estado, String usuarioAuditoria) throws SQLException;

    void resetearPassword(int idUsuario, String passwordNuevo, String usuarioAuditoria) throws SQLException;

    boolean sesionSigueVigente(int idUsuario, int sessionVersion) throws SQLException;
}
