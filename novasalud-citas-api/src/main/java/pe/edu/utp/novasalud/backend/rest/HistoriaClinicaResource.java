package pe.edu.utp.novasalud.backend.rest;

import pe.edu.utp.novasalud.backend.model.HistoriaClinica;
import pe.edu.utp.novasalud.backend.service.HistoriaClinicaService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Path("/historia-clinica")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HistoriaClinicaResource {

    private final HistoriaClinicaService historiaService = new HistoriaClinicaService();

    @GET
    public Response listar(@QueryParam("filtro") String filtro) {
        try {
            List<HistoriaClinica> lista = historiaService.listarHistorias(filtro);
            return Response.ok(lista).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("/paciente/{idPaciente}")
    public Response obtenerPorPaciente(@PathParam("idPaciente") int idPaciente) {
        try {
            HistoriaClinica historia = historiaService.obtenerHistoriaConAtencionesPorPaciente(idPaciente);
            if (historia == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(historia).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("/codigo/{codigoHistoria}")
    public Response obtenerPorCodigo(@PathParam("codigoHistoria") String codigoHistoria) {
        try {
            HistoriaClinica historia = historiaService.obtenerHistoriaConAtencionesPorCodigo(codigoHistoria);
            if (historia == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(historia).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @PUT
    @Path("/paciente/{idPaciente}")
    public Response actualizarAntecedentes(@PathParam("idPaciente") int idPaciente,
                                           @QueryParam("usuarioAudit") String usuarioAudit,
                                           Map<String, String> payload) {
        try {
            String antecedentes = payload != null ? payload.get("antecedentesGenerales") : "";
            String alergias = payload != null ? payload.get("alergias") : "";
            boolean ok = historiaService.actualizarAntecedentesYAlergias(idPaciente, antecedentes, alergias, usuarioAudit);
            if (!ok) {
                return Response.status(Response.Status.NOT_FOUND).entity("No se encontró la historia clínica para actualizar.").build();
            }
            return Response.ok(Map.of("mensaje", "Antecedentes y alergias actualizados correctamente.")).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
}
