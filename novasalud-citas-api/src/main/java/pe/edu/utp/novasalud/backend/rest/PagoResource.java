package pe.edu.utp.novasalud.backend.rest;

import pe.edu.utp.novasalud.backend.model.Pago;
import pe.edu.utp.novasalud.backend.service.PagoService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Path("/pagos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PagoResource {

    private final PagoService pagoService = new PagoService();

    @GET
    public Response listar(@QueryParam("fechaInicio") String fechaInicio,
                           @QueryParam("fechaFin") String fechaFin,
                           @QueryParam("estadoPago") String estadoPago,
                           @QueryParam("dniPaciente") String dniPaciente) {
        try {
            List<Pago> lista;
            if (dniPaciente != null && !dniPaciente.isBlank()) {
                lista = pagoService.listar(fechaInicio, fechaFin, estadoPago, dniPaciente);
            } else {
                lista = pagoService.listar(fechaInicio, fechaFin, estadoPago);
            }
            return Response.ok(lista).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") int id) {
        try {
            Pago pago = pagoService.obtenerPorId(id);
            if (pago == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(pago).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @POST
    @Path("/{id}/cobro")
    public Response registrarPago(@PathParam("id") int idPago, Map<String, Object> body) {
        try {
            BigDecimal monto = new BigDecimal(String.valueOf(body.get("monto")));
            String metodo = (String) body.get("metodo");
            String operacion = (String) body.get("operacion");
            String observacion = (String) body.get("observacion");
            int idUsuario = Integer.parseInt(String.valueOf(body.get("idUsuario")));

            String resultado = pagoService.registrarPago(idPago, monto, metodo, operacion, observacion, idUsuario);
            return Response.ok(resultado).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @POST
    @Path("/{id}/anular")
    public Response anularPago(@PathParam("id") int idPago, Map<String, String> body) {
        try {
            String motivo = body != null ? body.get("motivo") : null;
            pagoService.anularPago(idPago, motivo);
            return Response.ok().build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
}
