package pe.edu.utp.novasalud.backend.rest;

import pe.edu.utp.novasalud.backend.dao.UsuarioDAO;
import pe.edu.utp.novasalud.backend.dao.UsuarioDAOImpl;
import pe.edu.utp.novasalud.backend.model.Usuario;
import pe.edu.utp.novasalud.backend.util.JwtUtil;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    private final UsuarioDAO usuarioDAO = new UsuarioDAOImpl();

    @POST
    @Path("/login")
    public Response login(Map<String, String> credentials) {
        try {
            String username = credentials != null ? credentials.get("username") : null;
            String password = credentials != null ? credentials.get("password") : null;

            if (username == null || password == null) {
                Map<String, Object> err = new HashMap<>();
                err.put("error", "Credenciales incompletas");
                return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
            }

            Usuario usuario = usuarioDAO.validarCredenciales(username, password);
            if (usuario == null) {
                Map<String, Object> err = new HashMap<>();
                err.put("error", "Usuario o contraseña incorrectos");
                return Response.status(Response.Status.UNAUTHORIZED).entity(err).build();
            }

            String token = JwtUtil.generarToken(usuario);
            Map<String, Object> res = new HashMap<>();
            res.put("token", token);
            res.put("usuario", usuario);

            return Response.ok(res).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("/validar-sesion")
    public Response validarSesion(@QueryParam("idUsuario") int idUsuario, @QueryParam("sessionVersion") int sessionVersion) {
        try {
            boolean vigente = usuarioDAO.sesionSigueVigente(idUsuario, sessionVersion);
            Map<String, Object> res = new HashMap<>();
            res.put("vigente", vigente);
            return Response.ok(res).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
}
