package pe.edu.utp.novasalud.backend.rest;

import pe.edu.utp.novasalud.backend.model.Usuario;
import pe.edu.utp.novasalud.backend.service.UsuarioService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    private final UsuarioService usuarioService = new UsuarioService();

    @GET
    public Response listar(@QueryParam("filtro") String filtro) {
        try {
            List<Usuario> lista = usuarioService.listar(filtro);
            return Response.ok(lista).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") int id) {
        try {
            Usuario usuario = usuarioService.obtenerPorId(id);
            if (usuario == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(usuario).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @POST
    public Response guardar(@QueryParam("passwordInicial") String passwordInicial,
                            @QueryParam("auditoria") String auditoria,
                            Usuario usuario) {
        try {
            usuarioService.guardar(usuario, passwordInicial, auditoria);
            return Response.status(Response.Status.CREATED).entity(usuario).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}/estado")
    public Response cambiarEstado(@PathParam("id") int id, Map<String, String> body) {
        try {
            String estado = body != null ? body.get("estado") : null;
            String auditoria = body != null ? body.get("auditoria") : null;
            usuarioService.cambiarEstado(id, estado, auditoria);
            return Response.ok().build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}/password")
    public Response resetearPassword(@PathParam("id") int id, Map<String, String> body) {
        try {
            String passNuevo = body != null ? body.get("password") : null;
            String auditoria = body != null ? body.get("auditoria") : null;
            usuarioService.resetearPassword(id, passNuevo, auditoria);
            return Response.ok().build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
}
