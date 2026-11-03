package pe.edu.utp.novasalud.backend.rest;

import pe.edu.utp.novasalud.backend.model.Paciente;
import pe.edu.utp.novasalud.backend.service.PacienteService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/pacientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PacienteResource {

    private final PacienteService pacienteService = new PacienteService();

    @GET
    public Response listar(@QueryParam("filtro") String filtro) {
        try {
            List<Paciente> lista = pacienteService.listar(filtro);
            return Response.ok(lista).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("/{id: \\d+}")
    public Response obtenerPorId(@PathParam("id") int id) {
        try {
            Paciente paciente = pacienteService.obtenerPorId(id);
            if (paciente == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(paciente).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("/buscar/dni/{dni}")
    public Response obtenerPorDni(@PathParam("dni") String dni) {
        try {
            Paciente paciente = pacienteService.obtenerPorDni(dni);
            if (paciente == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(paciente).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("/dni/{dni}")
    public Response obtenerPorDniAlias(@PathParam("dni") String dni) {
        return obtenerPorDni(dni);
    }

    @POST
    public Response guardar(@QueryParam("usuario") String usuario, Paciente paciente) {
        try {
            pacienteService.guardar(paciente, usuario);
            return Response.status(Response.Status.CREATED).entity(paciente).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") int id) {
        try {
            pacienteService.eliminar(id);
            return Response.noContent().build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
}
