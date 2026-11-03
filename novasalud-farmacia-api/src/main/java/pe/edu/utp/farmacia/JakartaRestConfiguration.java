package pe.edu.utp.farmacia;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Configuración principal de servicios RESTful (JAX-RS) y especificación OpenAPI 3.0 para NovaSalud Farmacia Backend API.
 */
@ApplicationPath("/api")
@OpenAPIDefinition(
        info = @Info(
                title = "NovaSalud Farmacia Backend REST API",
                version = "1.0.0",
                description = "Documentación interactiva OpenAPI 3.0 para la API REST del Módulo de Farmacia de NovaSalud. Soporta autenticación mediante tokens JWT.",
                contact = @Contact(name = "Equipo NovaSalud UTP", email = "contacto@novasalud.pe")
        ),
        security = @SecurityRequirement(name = "BearerAuth")
)
@SecurityScheme(
        name = "BearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Ingrese el token JWT obtenido desde POST /api/auth/login"
)
public class JakartaRestConfiguration extends Application {

}
