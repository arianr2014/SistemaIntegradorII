package pe.edu.utp.farmacia.frontend.util;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class AppLogger {

    private AppLogger() {
    }

    public static void info(String message) {
        info(AppLogger.class, message);
    }

    public static void info(Class<?> clazz, String message) {
        Logger logger = Logger.getLogger(clazz.getName());
        logger.log(Level.INFO, message);
    }

    public static void warning(String message, Throwable throwable) {
        warning(AppLogger.class, message, throwable);
    }

    public static void warning(Class<?> clazz, String message, Throwable throwable) {
        Logger logger = Logger.getLogger(clazz.getName());
        logger.log(Level.WARNING, message, throwable);
    }

    public static void error(String message, Throwable throwable) {
        error(AppLogger.class, message, throwable);
    }

    public static void error(Class<?> clazz, String message, Throwable throwable) {
        Logger logger = Logger.getLogger(clazz.getName());
        logger.log(Level.SEVERE, message, throwable);
    }
}
