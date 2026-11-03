package pe.edu.utp.farmacia.rest;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import pe.edu.utp.farmacia.util.JwtUtil;
import java.io.IOException;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtAuthFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();

        // Rutas públicas omitidas de filtro JWT
        if (path.contains("auth/login") || path.contains("openapi") || path.contains("swagger") || requestContext.getMethod().equalsIgnoreCase("OPTIONS")) {
            return;
        }

        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\": \"Acceso no autorizado. Token JWT faltante o inválido.\"}")
                    .build());
            return;
        }

        String token = authHeader.substring("Bearer ".length()).trim();

        if (!JwtUtil.esTokenValido(token)) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\": \"Token JWT expirado o inválido.\"}")
                    .build());
        }
    }
}
