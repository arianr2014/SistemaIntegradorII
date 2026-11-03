package pe.edu.utp.novasalud.frontend.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Cliente HTTP REST para comunicación desde los Servlets Controllers (Frontend Web)
 * hacia la API Backend en Java con soporte de token JWT y fallback dinámico de URL.
 */
public final class ApiClient {

    private static final String DEFAULT_BASE_URL = "http://localhost:8080/novasalud-citas-api/api";
    private static final String CONFIG_FILE = "api.properties";
    private static String fileConfiguredUrl = null;

    static {
        loadConfig();
    }

    private static void loadConfig() {
        try (InputStream is = ApiClient.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (is != null) {
                Properties props = new Properties();
                props.load(is);
                String url = props.getProperty("api.base.url");
                if (url != null && !url.isBlank()) {
                    fileConfiguredUrl = url.trim().replaceAll("/+$", "");
                }
            }
        } catch (Exception ignored) {
        }
    }

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();
    private static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();

    private ApiClient() {
    }

    public static String getBaseUrl() {
        if (fileConfiguredUrl != null && !fileConfiguredUrl.isBlank()) {
            return fileConfiguredUrl;
        }
        String envUrl = System.getenv("NOVASALUD_BACKEND_URL");
        if (envUrl != null && !envUrl.isBlank()) {
            return envUrl.trim().replaceAll("/+$", "");
        }
        String propUrl = System.getProperty("novasalud.backend.url");
        if (propUrl != null && !propUrl.isBlank()) {
            return propUrl.trim().replaceAll("/+$", "");
        }
        return DEFAULT_BASE_URL;
    }

    private static List<String> buildUrlCandidates(String endpointPath) {
        String path = endpointPath.startsWith("/") ? endpointPath : "/" + endpointPath;
        String primary = getBaseUrl() + path;
        List<String> list = new ArrayList<>();
        list.add(primary);

        String alt = null;
        if (primary.contains("/novasalud-citas-api/")) {
            alt = primary.replace("/novasalud-citas-api/", "/novasalud-backend-api/");
        } else if (primary.contains("/novasalud-backend-api/")) {
            alt = primary.replace("/novasalud-backend-api/", "/novasalud-citas-api/");
        }

        if (alt != null && !alt.equals(primary)) {
            list.add(alt);
        }
        return list;
    }

    private static String extractBaseUrl(String fullUrl, String endpointPath) {
        String path = endpointPath.startsWith("/") ? endpointPath : "/" + endpointPath;
        if (fullUrl.endsWith(path)) {
            return fullUrl.substring(0, fullUrl.length() - path.length());
        }
        return fullUrl;
    }

    private static String resolveToken(String explicitToken) {
        if (explicitToken != null && !explicitToken.isBlank()) {
            return explicitToken.trim();
        }
        return JwtTokenContext.getToken();
    }

    public static <T> T get(String endpointPath, Class<T> responseType) throws Exception {
        return get(endpointPath, null, responseType);
    }

    public static <T> T get(String endpointPath, Type typeOfT) throws Exception {
        return get(endpointPath, null, typeOfT);
    }

    public static <T> T get(String endpointPath, String token, Type typeOfT) throws Exception {
        List<String> candidates = buildUrlCandidates(endpointPath);
        Exception lastException = null;

        for (String fullUrl : candidates) {
            try {
                HttpRequest.Builder builder = HttpRequest.newBuilder()
                        .uri(URI.create(fullUrl))
                        .header("Accept", "application/json")
                        .GET();

                String activeToken = resolveToken(token);
                if (activeToken != null && !activeToken.isBlank()) {
                    builder.header("Authorization", "Bearer " + activeToken);
                }

                HttpResponse<String> response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() >= 200 && response.statusCode() < 300) {
                    fileConfiguredUrl = extractBaseUrl(fullUrl, endpointPath);
                    return gson.fromJson(response.body(), typeOfT);
                }
                lastException = new RuntimeException("Error en petición REST GET [" + response.statusCode() + "]: " + response.body());
            } catch (Exception ex) {
                lastException = ex;
            }
        }
        throw lastException != null ? lastException : new RuntimeException("No se pudo conectar a la API Backend.");
    }

    public static <T> T get(String endpointPath, String token, Class<T> responseType) throws Exception {
        List<String> candidates = buildUrlCandidates(endpointPath);
        Exception lastException = null;

        for (String fullUrl : candidates) {
            try {
                HttpRequest.Builder builder = HttpRequest.newBuilder()
                        .uri(URI.create(fullUrl))
                        .header("Accept", "application/json")
                        .GET();

                String activeToken = resolveToken(token);
                if (activeToken != null && !activeToken.isBlank()) {
                    builder.header("Authorization", "Bearer " + activeToken);
                }

                HttpResponse<String> response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() >= 200 && response.statusCode() < 300) {
                    fileConfiguredUrl = extractBaseUrl(fullUrl, endpointPath);
                    if (responseType == String.class) {
                        return responseType.cast(response.body());
                    }
                    return gson.fromJson(response.body(), responseType);
                }
                lastException = new RuntimeException("Error en petición REST GET [" + response.statusCode() + "]: " + response.body());
            } catch (Exception ex) {
                lastException = ex;
            }
        }
        throw lastException != null ? lastException : new RuntimeException("No se pudo conectar a la API Backend.");
    }

    public static <T> List<T> getList(String endpointPath, Type listType) throws Exception {
        return getList(endpointPath, null, listType);
    }

    public static <T> List<T> getList(String endpointPath, String token, Type listType) throws Exception {
        List<String> candidates = buildUrlCandidates(endpointPath);
        Exception lastException = null;

        for (String fullUrl : candidates) {
            try {
                HttpRequest.Builder builder = HttpRequest.newBuilder()
                        .uri(URI.create(fullUrl))
                        .header("Accept", "application/json")
                        .GET();

                String activeToken = resolveToken(token);
                if (activeToken != null && !activeToken.isBlank()) {
                    builder.header("Authorization", "Bearer " + activeToken);
                }

                HttpResponse<String> response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() >= 200 && response.statusCode() < 300) {
                    fileConfiguredUrl = extractBaseUrl(fullUrl, endpointPath);
                    return gson.fromJson(response.body(), listType);
                }
                lastException = new RuntimeException("Error en petición REST GET List [" + response.statusCode() + "]: " + response.body());
            } catch (Exception ex) {
                lastException = ex;
            }
        }
        throw lastException != null ? lastException : new RuntimeException("No se pudo conectar a la API Backend.");
    }

    public static <T> T post(String endpointPath, Object bodyObject, Class<T> responseType) throws Exception {
        return post(endpointPath, bodyObject, null, responseType);
    }

    public static <T> T post(String endpointPath, Object bodyObject, String token, Class<T> responseType) throws Exception {
        List<String> candidates = buildUrlCandidates(endpointPath);
        Exception lastException = null;
        String jsonBody = bodyObject != null ? gson.toJson(bodyObject) : "";

        for (String fullUrl : candidates) {
            try {
                HttpRequest.Builder builder = HttpRequest.newBuilder()
                        .uri(URI.create(fullUrl))
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(jsonBody));

                String activeToken = resolveToken(token);
                if (activeToken != null && !activeToken.isBlank()) {
                    builder.header("Authorization", "Bearer " + activeToken);
                }

                HttpResponse<String> response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() >= 200 && response.statusCode() < 300) {
                    fileConfiguredUrl = extractBaseUrl(fullUrl, endpointPath);
                    if (responseType == Void.class || responseType == void.class) {
                        return null;
                    }
                    if (responseType == String.class) {
                        return responseType.cast(response.body());
                    }
                    return gson.fromJson(response.body(), responseType);
                }
                lastException = new RuntimeException("Error en petición REST POST [" + response.statusCode() + "]: " + response.body());
            } catch (Exception ex) {
                lastException = ex;
            }
        }
        throw lastException != null ? lastException : new RuntimeException("No se pudo conectar a la API Backend.");
    }

    public static <T> T put(String endpointPath, Object bodyObject, Class<T> responseType) throws Exception {
        return put(endpointPath, bodyObject, null, responseType);
    }

    public static <T> T put(String endpointPath, Object bodyObject, String token, Class<T> responseType) throws Exception {
        List<String> candidates = buildUrlCandidates(endpointPath);
        Exception lastException = null;
        String jsonBody = bodyObject != null ? gson.toJson(bodyObject) : "";

        for (String fullUrl : candidates) {
            try {
                HttpRequest.Builder builder = HttpRequest.newBuilder()
                        .uri(URI.create(fullUrl))
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .PUT(HttpRequest.BodyPublishers.ofString(jsonBody));

                String activeToken = resolveToken(token);
                if (activeToken != null && !activeToken.isBlank()) {
                    builder.header("Authorization", "Bearer " + activeToken);
                }

                HttpResponse<String> response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() >= 200 && response.statusCode() < 300) {
                    fileConfiguredUrl = extractBaseUrl(fullUrl, endpointPath);
                    if (responseType == Void.class || responseType == void.class) {
                        return null;
                    }
                    if (responseType == String.class) {
                        return responseType.cast(response.body());
                    }
                    return gson.fromJson(response.body(), responseType);
                }
                lastException = new RuntimeException("Error en petición REST PUT [" + response.statusCode() + "]: " + response.body());
            } catch (Exception ex) {
                lastException = ex;
            }
        }
        throw lastException != null ? lastException : new RuntimeException("No se pudo conectar a la API Backend.");
    }

    public static boolean delete(String endpointPath) throws Exception {
        return delete(endpointPath, null);
    }

    public static boolean delete(String endpointPath, String token) throws Exception {
        List<String> candidates = buildUrlCandidates(endpointPath);
        for (String fullUrl : candidates) {
            try {
                HttpRequest.Builder builder = HttpRequest.newBuilder()
                        .uri(URI.create(fullUrl))
                        .DELETE();

                String activeToken = resolveToken(token);
                if (activeToken != null && !activeToken.isBlank()) {
                    builder.header("Authorization", "Bearer " + activeToken);
                }

                HttpResponse<String> response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() >= 200 && response.statusCode() < 300) {
                    fileConfiguredUrl = extractBaseUrl(fullUrl, endpointPath);
                    return true;
                }
            } catch (Exception ignored) {
            }
        }
        return false;
    }
}
