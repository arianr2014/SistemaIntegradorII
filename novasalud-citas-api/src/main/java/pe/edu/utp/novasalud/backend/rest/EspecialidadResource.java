package pe.edu.utp.novasalud.backend.rest;

import pe.edu.utp.novasalud.backend.model.Especialidad;
import pe.edu.utp.novasalud.backend.service.EspecialidadService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/especialidades")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EspecialidadResource {

    private final EspecialidadService especialidadService = new EspecialidadService();

    @GET
    public Response listar(@QueryParam("filtro") String filtro) {
        try {
            List<Especialidad> lista = especialidadService.listar(filtro);
            return Response.ok(lista).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") int id) {
        try {
            Especialidad esp = especialidadService.obtenerPorId(id);
            if (esp == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(esp).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @POST
    public Response guardar(@QueryParam("usuario") String usuario, Especialidad esp) {
        try {
            especialidadService.guardar(esp, usuario);
            return Response.status(Response.Status.CREATED).entity(esp).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") int id) {
        try {
            especialidadService.eliminar(id);
            return Response.noContent().build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
}
