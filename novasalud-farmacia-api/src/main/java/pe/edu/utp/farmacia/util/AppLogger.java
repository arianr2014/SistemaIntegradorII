package pe.edu.utp.farmacia.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AppLogger {

    private AppLogger() {
    }

    public static void info(Class<?> clazz, String message) {
        Logger logger = LoggerFactory.getLogger(clazz);
        logger.info(message);
    }

    public static void warning(Class<?> clazz, String message, Throwable throwable) {
        Logger logger = LoggerFactory.getLogger(clazz);
        if (throwable != null) {
            logger.warn(message, throwable);
        } else {
            logger.warn(message);
        }
    }

    public static void error(Class<?> clazz, String message, Throwable throwable) {
        Logger logger = LoggerFactory.getLogger(clazz);
        if (throwable != null) {
            logger.error(message, throwable);
        } else {
            logger.error(message);
        }
    }
}
