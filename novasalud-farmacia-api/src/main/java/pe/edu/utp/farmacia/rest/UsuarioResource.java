package pe.edu.utp.farmacia.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.utp.farmacia.model.UsuarioFarmacia;
import pe.edu.utp.farmacia.service.UsuarioFarmaciaService;
import java.util.List;
import java.util.Map;

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    private final UsuarioFarmaciaService service = new UsuarioFarmaciaService();

    @GET
    public Response listar() {
        try {
            List<UsuarioFarmacia> lista = service.listar();
            return Response.ok(lista).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") int id) {
        try {
            UsuarioFarmacia u = service.obtenerPorId(id);
            if (u == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("error", "Usuario no encontrado"))
                        .build();
            }
            return Response.ok(u).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    @POST
    public Response registrar(Map<String, Object> body, @QueryParam("usuario") @DefaultValue("sistema") String usuarioRegistro) {
        try {
            UsuarioFarmacia u = new UsuarioFarmacia();
            u.setUsername((String) body.get("username"));
            u.setNombres((String) body.get("nombres"));
            u.setApellidos((String) body.get("apellidos"));
            u.setRol((String) body.get("rol"));
            String rawPassword = (String) body.get("password");

            int id = service.registrar(u, rawPassword, usuarioRegistro);
            u.setIdUsuario(id);
            u.setPasswordHash(null);
            return Response.status(Response.Status.CREATED).entity(u).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("error", e.getMessage())).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("error", e.getMessage())).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") int id, Map<String, Object> body, @QueryParam("usuario") @DefaultValue("sistema") String usuarioActualizacion) {
        try {
            UsuarioFarmacia u = new UsuarioFarmacia();
            u.setIdUsuario(id);
            u.setUsername((String) body.get("username"));
            u.setNombres((String) body.get("nombres"));
            u.setApellidos((String) body.get("apellidos"));
            u.setRol((String) body.get("rol"));
            String newPassword = (String) body.get("password");

            service.actualizar(u, newPassword, usuarioActualizacion);
            return Response.ok(Map.of("mensaje", "Usuario actualizado exitosamente")).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("error", e.getMessage())).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("error", e.getMessage())).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") int id, @QueryParam("usuario") @DefaultValue("sistema") String usuario) {
        try {
            service.eliminar(id, usuario);
            return Response.ok(Map.of("mensaje", "Usuario eliminado exitosamente")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("error", e.getMessage())).build();
        }
    }
}
