package pe.edu.utp.farmacia.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.utp.farmacia.model.CategoriaMedicamento;
import pe.edu.utp.farmacia.service.CategoriaMedicamentoService;
import java.util.List;
import java.util.Map;

@Path("/categorias")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoriaResource {

    private final CategoriaMedicamentoService service = new CategoriaMedicamentoService();

    @GET
    public Response listar() {
        try {
            List<CategoriaMedicamento> lista = service.listar();
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
            CategoriaMedicamento c = service.obtenerPorId(id);
            if (c == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("error", "Categoría no encontrada"))
                        .build();
            }
            return Response.ok(c).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    @POST
    public Response registrar(CategoriaMedicamento c, @QueryParam("usuario") @DefaultValue("sistema") String usuario) {
        try {
            int id = service.registrar(c, usuario);
            c.setIdCategoria(id);
            return Response.status(Response.Status.CREATED).entity(c).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("error", e.getMessage())).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("error", e.getMessage())).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") int id, CategoriaMedicamento c, @QueryParam("usuario") @DefaultValue("sistema") String usuario) {
        try {
            c.setIdCategoria(id);
            service.actualizar(c, usuario);
            return Response.ok(Map.of("mensaje", "Categoría actualizada exitosamente")).build();
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
            return Response.ok(Map.of("mensaje", "Categoría eliminada exitosamente")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("error", e.getMessage())).build();
        }
    }
}
