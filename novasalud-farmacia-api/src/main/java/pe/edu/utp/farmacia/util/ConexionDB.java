package pe.edu.utp.farmacia.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Singleton de alto rendimiento para el pool de conexiones HikariCP apuntando a bd_famacia.
 */
public final class ConexionDB {

    private static final Properties FILE_PROPERTIES = new Properties();
    private static final HikariDataSource dataSource;

    static {
        try (InputStream input = ConexionDB.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input != null) {
                FILE_PROPERTIES.load(input);
            } else {
                AppLogger.warning(ConexionDB.class, "No se encontró db.properties en el classpath de Farmacia. Se usará fallback por defecto.", null);
            }
        } catch (IOException ex) {
            AppLogger.error(ConexionDB.class, "Error al leer db.properties en módulo Farmacia", ex);
        }

        String driverClass = readProperty("db.driver", "com.mysql.cj.jdbc.Driver");
        try {
            Class.forName(driverClass);
        } catch (ClassNotFoundException ex) {
            throw new ExceptionInInitializerError("No se encontró el driver JDBC MySQL: " + driverClass);
        }

        String url = readConfig("NOVASALUD_FARMACIA_DB_URL", "DB_URL", "db.url",
                "jdbc:mysql://localhost:3306/bd_famacia?useSSL=false&allowPublicKeyRetrieval=true&noAccessToProcedureBodies=true&serverTimezone=UTC");
        String user = readConfig("NOVASALUD_FARMACIA_DB_USER", "DB_USER", "db.user", "usuario_farmacia");
        String password = readConfig("NOVASALUD_FARMACIA_DB_PASSWORD", "DB_PASSWORD", "db.password", "Farmacia2026*");

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        config.setDriverClassName(driverClass);

        config.setMaximumPoolSize(readIntProperty("db.pool.maximumPoolSize", 10));
        config.setMinimumIdle(readIntProperty("db.pool.minimumIdle", 2));
        config.setIdleTimeout(readLongProperty("db.pool.idleTimeout", 30000L));
        config.setConnectionTimeout(readLongProperty("db.pool.connectionTimeout", 10000L));
        config.setMaxLifetime(readLongProperty("db.pool.maxLifetime", 1800000L));
        config.setPoolName("NovaSaludFarmaciaHikariCP");

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.addDataSourceProperty("noAccessToProcedureBodies", "true");

        dataSource = new HikariDataSource(config);
        AppLogger.info(ConexionDB.class, "Pool de conexiones HikariCP (bd_famacia) inicializado exitosamente.");
    }

    private ConexionDB() {
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static Connection getConexion() throws SQLException {
        return getConnection();
    }

    public static void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            AppLogger.info(ConexionDB.class, "Pool de conexiones HikariCP de Farmacia cerrado limpiamente.");
        }
    }

    private static String readConfig(String specificEnvKey, String genericEnvKey, String propertyKey, String defaultValue) {
        String specificEnvValue = System.getenv(specificEnvKey);
        if (specificEnvValue != null && !specificEnvValue.isBlank()) {
            return specificEnvValue.trim();
        }
        String genericEnvValue = System.getenv(genericEnvKey);
        if (genericEnvValue != null && !genericEnvValue.isBlank()) {
            return genericEnvValue.trim();
        }
        String sysProperty = System.getProperty(propertyKey);
        if (sysProperty != null && !sysProperty.isBlank()) {
            return sysProperty.trim();
        }
        return readProperty(propertyKey, defaultValue);
    }

    private static String readProperty(String key, String defaultValue) {
        String val = FILE_PROPERTIES.getProperty(key);
        return (val != null && !val.isBlank()) ? val.trim() : defaultValue;
    }

    private static int readIntProperty(String key, int defaultValue) {
        try {
            String val = readProperty(key, null);
            return val != null ? Integer.parseInt(val) : defaultValue;
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    private static long readLongProperty(String key, long defaultValue) {
        try {
            String val = readProperty(key, null);
            return val != null ? Long.parseLong(val) : defaultValue;
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }
}
