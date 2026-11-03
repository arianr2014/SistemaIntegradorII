package pe.edu.utp.farmacia.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.utp.farmacia.model.Proveedor;
import pe.edu.utp.farmacia.service.ProveedorService;
import java.util.List;
import java.util.Map;

@Path("/proveedores")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProveedorResource {

    private final ProveedorService service = new ProveedorService();

    @GET
    public Response listar() {
        try {
            List<Proveedor> lista = service.listar();
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
            Proveedor p = service.obtenerPorId(id);
            if (p == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("error", "Proveedor no encontrado"))
                        .build();
            }
            return Response.ok(p).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    @POST
    public Response registrar(Proveedor p, @QueryParam("usuario") @DefaultValue("sistema") String usuario) {
        try {
            int id = service.registrar(p, usuario);
            p.setIdProveedor(id);
            return Response.status(Response.Status.CREATED).entity(p).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("error", e.getMessage())).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("error", e.getMessage())).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") int id, Proveedor p, @QueryParam("usuario") @DefaultValue("sistema") String usuario) {
        try {
            p.setIdProveedor(id);
            service.actualizar(p, usuario);
            return Response.ok(Map.of("mensaje", "Proveedor actualizado exitosamente")).build();
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
            return Response.ok(Map.of("mensaje", "Proveedor eliminado exitosamente")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("error", e.getMessage())).build();
        }
    }
}
