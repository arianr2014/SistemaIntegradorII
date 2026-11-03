package pe.edu.utp.farmacia.rest;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.utp.farmacia.model.UsuarioFarmacia;
import pe.edu.utp.farmacia.service.UsuarioFarmaciaService;
import pe.edu.utp.farmacia.util.JwtUtil;
import java.util.HashMap;
import java.util.Map;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    private final UsuarioFarmaciaService usuarioService = new UsuarioFarmaciaService();

    @POST
    @Path("/login")
    public Response login(Map<String, String> credenciales) {
        if (credenciales == null || !credenciales.containsKey("username") || !credenciales.containsKey("password")) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Se requiere username y password"))
                    .build();
        }

        try {
            UsuarioFarmacia usuario = usuarioService.autenticar(credenciales.get("username"), credenciales.get("password"));

            if (usuario == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(Map.of("error", "Credenciales inválidas"))
                        .build();
            }

            String token = JwtUtil.generarToken(usuario.getUsername(), usuario.getRol(), usuario.getIdUsuario());

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("type", "Bearer");
            response.put("idUsuario", usuario.getIdUsuario());
            response.put("username", usuario.getUsername());
            response.put("nombres", usuario.getNombres());
            response.put("apellidos", usuario.getApellidos());
            response.put("rol", usuario.getRol());

            return Response.ok(response).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Error al procesar autenticación: " + e.getMessage()))
                    .build();
        }
    }
}
