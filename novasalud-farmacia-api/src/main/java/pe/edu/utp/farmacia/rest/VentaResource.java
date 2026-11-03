package pe.edu.utp.farmacia.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.utp.farmacia.model.Venta;
import pe.edu.utp.farmacia.service.VentaService;
import java.util.List;
import java.util.Map;

@Path("/ventas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VentaResource {

    private final VentaService service = new VentaService();

    @GET
    public Response listar() {
        try {
            List<Venta> lista = service.listar();
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
            Venta v = service.obtenerPorId(id);
            if (v == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("error", "Venta no encontrada"))
                        .build();
            }
            return Response.ok(v).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    @POST
    public Response registrar(Venta v, @QueryParam("usuario") @DefaultValue("sistema") String usuario) {
        try {
            int id = service.registrar(v, usuario);
            v.setIdVenta(id);
            return Response.status(Response.Status.CREATED).entity(v).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("error", e.getMessage())).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("error", e.getMessage())).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") int id, Venta v, @QueryParam("usuario") @DefaultValue("sistema") String usuario) {
        try {
            v.setIdVenta(id);
            service.actualizar(v, usuario);
            return Response.ok(Map.of("mensaje", "Venta actualizada exitosamente")).build();
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
            return Response.ok(Map.of("mensaje", "Venta eliminada exitosamente")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("error", e.getMessage())).build();
        }
    }
}
