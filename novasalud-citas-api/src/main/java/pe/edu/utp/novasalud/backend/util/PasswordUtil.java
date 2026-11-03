package pe.edu.utp.novasalud.backend.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utilidad de contraseñas del sistema.
 *
 * V3.2:
 * - Se mantiene BCrypt como mecanismo único de validación.
 * - Se elimina la compatibilidad con contraseñas en texto plano usada solo durante migración.
 * - Esto evita que una contraseña plana insertada por error sea aceptada por el login.
 */
public final class PasswordUtil {

    private PasswordUtil() {
        // Clase utilitaria: no debe instanciarse.
    }

    public static String generarHash(String passwordPlano) {
        if (passwordPlano == null || passwordPlano.isBlank()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía.");
        }
        return BCrypt.hashpw(passwordPlano, BCrypt.gensalt(12));
    }

    public static boolean verificarPassword(String passwordPlano, String hashAlmacenado) {
        if (passwordPlano == null || hashAlmacenado == null || hashAlmacenado.isBlank()) {
            return false;
        }

        if (!esHashBCrypt(hashAlmacenado)) {
            return false;
        }

        return BCrypt.checkpw(passwordPlano, hashAlmacenado);
    }

    public static boolean esHashBCrypt(String value) {
        return value != null && (value.startsWith("$2a$") || value.startsWith("$2b$") || value.startsWith("$2y$"));
    }
}
