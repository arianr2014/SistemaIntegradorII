package pe.edu.utp.novasalud.frontend.util;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Logger centralizado para evitar mostrar errores técnicos al usuario final.
 *
 * V3.2:
 * - Los controladores muestran mensajes amigables.
 * - El detalle técnico queda en el log del servidor.
 */
public final class AppLogger {

    private AppLogger() {
        // Clase utilitaria.
    }

    public static void error(Class<?> origen, String mensaje, Throwable ex) {
        Logger.getLogger(origen.getName()).log(Level.SEVERE, mensaje, ex);
    }

    public static void warning(Class<?> origen, String mensaje, Throwable ex) {
        Logger.getLogger(origen.getName()).log(Level.WARNING, mensaje, ex);
    }
}
