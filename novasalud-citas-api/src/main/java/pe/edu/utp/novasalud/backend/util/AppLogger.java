package pe.edu.utp.novasalud.backend.util;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Logger centralizado para el backend de NovaSalud.
 */
public final class AppLogger {

    private AppLogger() {
    }

    public static void info(Class<?> origen, String mensaje) {
        Logger.getLogger(origen.getName()).log(Level.INFO, mensaje);
    }

    public static void warning(Class<?> origen, String mensaje, Throwable ex) {
        Logger.getLogger(origen.getName()).log(Level.WARNING, mensaje, ex);
    }

    public static void error(Class<?> origen, String mensaje, Throwable ex) {
        Logger.getLogger(origen.getName()).log(Level.SEVERE, mensaje, ex);
    }
}
