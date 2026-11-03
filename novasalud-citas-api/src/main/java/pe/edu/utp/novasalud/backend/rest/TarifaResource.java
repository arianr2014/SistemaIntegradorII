package pe.edu.utp.novasalud.backend.rest;

import pe.edu.utp.novasalud.backend.model.TarifaConsulta;
import pe.edu.utp.novasalud.backend.service.TarifaConsultaService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/tarifas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TarifaResource {

    private final TarifaConsultaService tarifaService = new TarifaConsultaService();

    @GET
    public Response listar() {
        try {
            List<TarifaConsulta> lista = tarifaService.listar();
            return Response.ok(lista).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") int id) {
        try {
            TarifaConsulta t = tarifaService.obtenerPorId(id);
            if (t == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(t).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @POST
    public Response guardar(@QueryParam("usuario") String usuario, TarifaConsulta tarifa) {
        try {
            tarifaService.guardar(tarifa, usuario);
            return Response.status(Response.Status.CREATED).entity(tarifa).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}/desactivar")
    public Response desactivar(@PathParam("id") int id, @QueryParam("usuario") String usuario) {
        try {
            tarifaService.desactivar(id, usuario);
            return Response.ok().build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
}
