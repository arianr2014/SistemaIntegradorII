package pe.edu.utp.novasalud.backend.rest;

import io.jsonwebtoken.Claims;
import pe.edu.utp.novasalud.backend.util.JwtUtil;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Filtro JAX-RS que intercepta todas las peticiones a la API REST
 * y exige un token JWT válido en la cabecera Authorization: Bearer <token>.
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtAuthFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();
        String method = requestContext.getMethod();

        // Permitir preflight CORS OPTIONS, login y especificación OpenAPI sin token
        if ("OPTIONS".equalsIgnoreCase(method) 
                || path.endsWith("auth/login") 
                || path.endsWith("auth/validar-sesion")
                || path.contains("openapi")) {
            return;
        }

        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            abortWithUnauthorized(requestContext, "Falta la cabecera de autorización Authorization: Bearer <token>");
            return;
        }

        String token = authHeader.substring("Bearer ".length()).trim();
        Claims claims = JwtUtil.validarToken(token);

        if (claims == null) {
            abortWithUnauthorized(requestContext, "Token JWT inválido o expirado");
        }
    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext, String mensaje) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", mensaje);

        Response response = Response.status(Response.Status.UNAUTHORIZED)
                .entity(errorMap)
                .type(MediaType.APPLICATION_JSON)
                .build();

        requestContext.abortWith(response);
    }
}
