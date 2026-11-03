package pe.edu.utp.novasalud.frontend.controller;

import com.google.gson.reflect.TypeToken;
import pe.edu.utp.novasalud.frontend.model.Pago;
import pe.edu.utp.novasalud.frontend.model.TarifaConsulta;
import pe.edu.utp.novasalud.frontend.util.ApiClient;
import pe.edu.utp.novasalud.frontend.util.AppLogger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Controlador de Pagos / Caja para Frontend Web Java. */
@WebServlet("/pagos")
public class PagoController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String accion = texto(request.getParameter("accion"));
            if ("cobrar".equals(accion)) {
                int idPago = parseInt(request.getParameter("id"));
                Pago pago = ApiClient.get("/pagos/" + idPago, Pago.class);
                List<TarifaConsulta> tarifas = ApiClient.getList("/tarifas", new TypeToken<List<TarifaConsulta>>(){}.getType());
                request.setAttribute("pago", pago);
                request.setAttribute("tarifasActivas", tarifas);
                request.getRequestDispatcher("/WEB-INF/views/pago/form.jsp").forward(request, response);
                return;
            }
            if ("comprobante".equals(accion)) {
                int idPago = parseInt(request.getParameter("id"));
                Pago pago = ApiClient.get("/pagos/" + idPago, Pago.class);
                request.setAttribute("pago", pago);
                request.getRequestDispatcher("/WEB-INF/views/pago/comprobante.jsp").forward(request, response);
                return;
            }
            listar(request, response);
        } catch (Exception ex) {
            AppLogger.error(getClass(), "Error cargando pagos vía REST API", ex);
            throw new ServletException("No se pudo cargar el módulo de pagos.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        try {
            String accion = texto(request.getParameter("accion"));
            if ("anular".equals(accion)) {
                int idPago = parseInt(request.getParameter("id"));
                Map<String, String> body = new HashMap<>();
                body.put("motivo", texto(request.getParameter("motivo")));
                ApiClient.post("/pagos/" + idPago + "/anular", body, Void.class);
                response.sendRedirect(request.getContextPath()+"/pagos?msg=annulled");
                return;
            }

            int idPago = parseInt(request.getParameter("idPago"));
            Map<String, Object> body = new HashMap<>();
            body.put("monto", request.getParameter("monto"));
            body.put("metodo", texto(request.getParameter("metodoPago")));
            body.put("operacion", texto(request.getParameter("numeroOperacion")));
            body.put("observacion", texto(request.getParameter("observacion")));
            body.put("idUsuario", idUsuario(request));

            String resultado = ApiClient.post("/pagos/" + idPago + "/cobro", body, String.class);
            response.sendRedirect(request.getContextPath()+"/pagos?msg=" + (resultado != null ? resultado : "ok"));
        } catch (Exception ex) {
            AppLogger.warning(getClass(), "Error registrando pago vía REST API", ex);
            response.sendRedirect(request.getContextPath()+"/pagos?msg=invalid");
        }
    }

    private void listar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fechaInicio = texto(request.getParameter("fechaInicio"));
        String fechaFin = texto(request.getParameter("fechaFin"));
        String estadoPago = texto(request.getParameter("estadoPago"));
        String dniPaciente = texto(request.getParameter("dniPaciente"));
        if (fechaInicio.isBlank()) fechaInicio = LocalDate.now().toString();
        if (fechaFin.isBlank()) fechaFin = LocalDate.now().toString();

        String query = "/pagos?fechaInicio=" + URLEncoder.encode(fechaInicio, StandardCharsets.UTF_8)
                + "&fechaFin=" + URLEncoder.encode(fechaFin, StandardCharsets.UTF_8)
                + "&estadoPago=" + URLEncoder.encode(estadoPago, StandardCharsets.UTF_8)
                + "&dniPaciente=" + URLEncoder.encode(dniPaciente, StandardCharsets.UTF_8);

        List<Pago> pagos = ApiClient.getList(query, new TypeToken<List<Pago>>(){}.getType());
        List<TarifaConsulta> tarifas = ApiClient.getList("/tarifas", new TypeToken<List<TarifaConsulta>>(){}.getType());

        request.setAttribute("pagos", pagos);
        request.setAttribute("tarifasActivas", tarifas);
        request.setAttribute("fechaInicio", fechaInicio);
        request.setAttribute("fechaFin", fechaFin);
        request.setAttribute("estadoPago", estadoPago);
        request.setAttribute("dniPaciente", dniPaciente);
        request.getRequestDispatcher("/WEB-INF/views/pago/list.jsp").forward(request, response);
    }

    private int idUsuario(HttpServletRequest request) { HttpSession s=request.getSession(false); if (s==null) return 0; Object v=s.getAttribute("idUsuario"); try { return Integer.parseInt(String.valueOf(v)); } catch(Exception e){ return 0; } }
    private String texto(String v) { return v == null ? "" : v.trim(); }
    private int parseInt(String v) { try { return Integer.parseInt(v); } catch(Exception e) { return 0; } }
}
