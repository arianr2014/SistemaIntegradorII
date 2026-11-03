package pe.edu.utp.novasalud.backend.rest;

import pe.edu.utp.novasalud.backend.model.Horario;
import pe.edu.utp.novasalud.backend.service.HorarioService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/horarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HorarioResource {

    private final HorarioService horarioService = new HorarioService();

    @GET
    public Response listar(@QueryParam("filtro") String filtro) {
        try {
            List<Horario> lista = horarioService.listar(filtro);
            return Response.ok(lista).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("/disponibles")
    public Response listarDisponibles(
            @QueryParam("idMedico") int idMedico,
            @QueryParam("fecha") String fecha,
            @QueryParam("idCitaExcluir") @DefaultValue("0") int idCitaExcluir
    ) {
        try {
            List<String> horas = horarioService.listarHorasDisponibles(idMedico, fecha, idCitaExcluir);
            return Response.ok(horas).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") int id) {
        try {
            Horario h = horarioService.obtenerPorId(id);
            if (h == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(h).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @POST
    public Response guardar(@QueryParam("usuario") String usuario, Horario horario) {
        try {
            horarioService.guardar(horario, usuario);
            return Response.status(Response.Status.CREATED).entity(horario).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") int id) {
        try {
            horarioService.eliminar(id);
            return Response.noContent().build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
}
