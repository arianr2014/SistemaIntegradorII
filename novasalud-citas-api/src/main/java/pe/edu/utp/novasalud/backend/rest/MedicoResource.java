package pe.edu.utp.novasalud.backend.rest;

import pe.edu.utp.novasalud.backend.model.Medico;
import pe.edu.utp.novasalud.backend.service.MedicoService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/medicos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MedicoResource {

    private final MedicoService medicoService = new MedicoService();

    @GET
    public Response listar(@QueryParam("filtro") String filtro) {
        try {
            List<Medico> lista = medicoService.listar(filtro);
            return Response.ok(lista).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") int id) {
        try {
            Medico medico = medicoService.obtenerPorId(id);
            if (medico == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(medico).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @POST
    public Response guardar(@QueryParam("usuario") String usuario, Medico medico) {
        try {
            medicoService.guardar(medico, usuario);
            return Response.status(Response.Status.CREATED).entity(medico).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") int id) {
        try {
            medicoService.eliminar(id);
            return Response.noContent().build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
}
