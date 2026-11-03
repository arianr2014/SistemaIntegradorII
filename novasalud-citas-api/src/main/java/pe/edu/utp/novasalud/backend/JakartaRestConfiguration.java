package pe.edu.utp.novasalud.backend;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Configuración principal de servicios RESTful (JAX-RS) y especificación OpenAPI 3.0 para NovaSalud Backend API.
 */
@ApplicationPath("/api")
@OpenAPIDefinition(
        info = @Info(
                title = "NovaSalud Backend REST API",
                version = "3.2.1",
                description = "Documentación interactiva de la API REST del Sistema de Citas Médicas NovaSalud V3.2.1. Incluye soporte para autenticación mediante tokens JWT.",
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
