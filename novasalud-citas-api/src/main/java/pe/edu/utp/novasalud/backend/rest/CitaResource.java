package pe.edu.utp.novasalud.backend.rest;

import pe.edu.utp.novasalud.backend.model.Cita;
import pe.edu.utp.novasalud.backend.service.CitaService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/citas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CitaResource {

    private final CitaService citaService = new CitaService();

    @GET
    public Response listarFiltrado(@QueryParam("dniPaciente") String dniPaciente,
                                  @QueryParam("idMedico") @DefaultValue("0") int idMedico,
                                  @QueryParam("idEspecialidad") @DefaultValue("0") int idEspecialidad) {
        try {
            List<Cita> lista = citaService.listarFiltrado(dniPaciente, idMedico, idEspecialidad);
            return Response.ok(lista).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") int id) {
        try {
            Cita cita = citaService.obtenerPorId(id);
            if (cita == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(cita).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("/agenda")
    public Response listarAgendaProgramada(@QueryParam("idMedico") int idMedico,
                                           @QueryParam("idEspecialidad") @DefaultValue("0") int idEspecialidad,
                                           @QueryParam("fecha") String fecha) {
        try {
            List<Cita> lista = citaService.listarAgendaProgramadaFlexible(idMedico, idEspecialidad, fecha);
            return Response.ok(lista).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("/resumen-mensual")
    public Response resumenMensualPorMedico(@QueryParam("idMedico") int idMedico,
                                            @QueryParam("inicio") String inicio,
                                            @QueryParam("fin") String fin) {
        try {
            java.util.Map<String, Integer> mapa = citaService.resumenMensualPorMedico(idMedico, inicio, fin);
            return Response.ok(mapa).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @POST
    public Response guardar(@QueryParam("usuario") String usuario,
                            @QueryParam("idRecepcionista") @DefaultValue("0") int idRecepcionista,
                            Cita cita) {
        try {
            citaService.guardar(cita, usuario, idRecepcionista);
            return Response.status(Response.Status.CREATED).entity(cita).build();
        } catch (Exception ex) {
            pe.edu.utp.novasalud.backend.util.AppLogger.error(CitaResource.class, "Error al guardar cita", ex);
            String msg = ex.getMessage() == null ? ex.toString() : ex.getMessage().replace("\"", "'");
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\":\"" + msg + "\"}").build();
        }
    }

    @PUT
    @Path("/{id}/atendida")
    public Response marcarAtendida(@PathParam("id") int id, @QueryParam("idMedico") @DefaultValue("0") int idMedico) {
        try {
            boolean ok;
            if (idMedico > 0) {
                ok = citaService.marcarComoAtendidaPorDoctor(id, idMedico);
            } else {
                ok = citaService.marcarComoAtendida(id);
            }
            return Response.ok(ok).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}/no-asistio")
    public Response marcarNoAsistio(@PathParam("id") int id) {
        try {
            boolean ok = citaService.marcarNoAsistio(id);
            return Response.ok(ok).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") int id) {
        try {
            citaService.eliminar(id);
            return Response.noContent().build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
}
