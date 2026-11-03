package pe.edu.utp.novasalud.backend.rest;

import pe.edu.utp.novasalud.backend.dao.CitaDAO;
import pe.edu.utp.novasalud.backend.dao.CitaDAOImpl;
import pe.edu.utp.novasalud.backend.service.PagoService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Path("/reportes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReporteResource {

    private final PagoService pagoService = new PagoService();
    private final CitaDAO citaDAO = new CitaDAOImpl();

    @GET
    @Path("/financiero")
    public Response financiero(@QueryParam("fechaInicio") String inicio, @QueryParam("fechaFin") String fin) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("totalIngresos", pagoService.totalIngresos(inicio, fin));
            data.put("porEspecialidad", pagoService.ingresosPorEspecialidad(inicio, fin));
            data.put("porMetodo", pagoService.ingresosPorMetodoPago(inicio, fin));
            return Response.ok(data).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("/estadisticas")
    public Response estadisticas(@QueryParam("fechaInicio") String inicio, @QueryParam("fechaFin") String fin) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("topEspecialidades", citaDAO.topEspecialidadesPorRango(inicio, fin));
            data.put("resumenSemanal", citaDAO.resumenSemanalPorDia(inicio, fin));
            return Response.ok(data).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("/dashboard")
    public Response dashboard() {
        try {
            LocalDate hoy = LocalDate.now();
            LocalDate inicioSemana = hoy.minusDays(6);

            Map<String, Integer> citasPorEstadoDb = citaDAO.contarCitasHoyPorEstado(hoy.toString());
            Map<String, Integer> citasPorEstadoHoy = new LinkedHashMap<>();
            citasPorEstadoHoy.put("PROGRAMADA", 0);
            citasPorEstadoHoy.put("ATENDIDA", 0);
            citasPorEstadoHoy.put("CANCELADA", 0);
            citasPorEstadoHoy.put("NO ASISTIO", 0);
            for (Map.Entry<String, Integer> entry : citasPorEstadoDb.entrySet()) {
                citasPorEstadoHoy.put(entry.getKey(), entry.getValue());
            }

            int medicosTurnoHoy = citaDAO.contarMedicosEnTurnoHoy(hoy.toString());

            Map<String, Integer> especialidadesDemanda = citaDAO.topEspecialidadesPorRango(inicioSemana.toString(), hoy.toString());
            List<Map.Entry<String, Integer>> topEspecialidades = new ArrayList<>(especialidadesDemanda.entrySet());
            if (topEspecialidades.size() > 3) {
                topEspecialidades = topEspecialidades.subList(0, 3);
            }

            Map<String, Integer> resumenSemanalDb = citaDAO.resumenSemanalPorDia(inicioSemana.toString(), hoy.toString());
            Map<String, Integer> resumenSemanal = new LinkedHashMap<>();
            for (int i = 0; i < 7; i++) {
                LocalDate fecha = inicioSemana.plusDays(i);
                String clave = fecha.toString();
                resumenSemanal.put(clave, resumenSemanalDb.getOrDefault(clave, 0));
            }

            Map<String, Object> data = new HashMap<>();
            data.put("citasPorEstadoHoy", citasPorEstadoHoy);
            data.put("medicosTurnoHoy", medicosTurnoHoy);
            data.put("topEspecialidades", topEspecialidades);
            data.put("resumenSemanal", resumenSemanal);
            data.put("fechaHoy", hoy.toString());

            return Response.ok(data).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("/direccion")
    public Response direccion() {
        try {
            LocalDate hoy = LocalDate.now();
            String inicioMes = hoy.withDayOfMonth(1).toString();

            Map<String, Object> data = new HashMap<>();
            data.put("fechaHoy", hoy.toString());
            data.put("citasHoy", citaDAO.contarCitasHoyPorEstado(hoy.toString()));
            data.put("ingresosMes", pagoService.totalIngresos(inicioMes, hoy.toString()));
            data.put("topEspecialidades", citaDAO.topEspecialidadesPorRango(inicioMes, hoy.toString()));
            data.put("ingresosEspecialidad", pagoService.ingresosPorEspecialidad(inicioMes, hoy.toString()));

            return Response.ok(data).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
}
