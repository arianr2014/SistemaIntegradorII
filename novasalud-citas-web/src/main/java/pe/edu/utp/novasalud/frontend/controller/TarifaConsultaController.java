package pe.edu.utp.novasalud.frontend.controller;

import com.google.gson.reflect.TypeToken;
import pe.edu.utp.novasalud.frontend.model.Especialidad;
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
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/** Controlador del módulo Tarifas para Frontend Web Java. */
@WebServlet("/tarifas")
public class TarifaConsultaController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String accion = texto(request.getParameter("accion"));
            if ("form".equals(accion) || "editar".equals(accion)) {
                int id = parseInt(request.getParameter("id"));
                if (id > 0) {
                    TarifaConsulta tarifa = ApiClient.get("/tarifas/" + id, TarifaConsulta.class);
                    request.setAttribute("tarifa", tarifa);
                }
                List<Especialidad> especialidades = ApiClient.getList("/especialidades", new TypeToken<List<Especialidad>>(){}.getType());
                request.setAttribute("especialidades", especialidades);
                request.getRequestDispatcher("/WEB-INF/views/tarifa/form.jsp").forward(request, response);
                return;
            }
            List<TarifaConsulta> tarifas = ApiClient.getList("/tarifas", new TypeToken<List<TarifaConsulta>>(){}.getType());
            request.setAttribute("tarifas", tarifas);
            request.getRequestDispatcher("/WEB-INF/views/tarifa/list.jsp").forward(request, response);
        } catch (Exception ex) {
            AppLogger.error(getClass(), "Error en tarifas vía REST API", ex);
            throw new ServletException("No se pudo cargar tarifas.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        try {
            String accion = texto(request.getParameter("accion"));
            String usuario = usuarioSesion(request);
            if ("desactivar".equals(accion)) {
                int id = parseInt(request.getParameter("id"));
                ApiClient.put("/tarifas/" + id + "/desactivar?usuario=" + URLEncoder.encode(usuario, StandardCharsets.UTF_8), null, Void.class);
                response.sendRedirect(request.getContextPath()+"/tarifas?msg=deleted");
                return;
            }
            TarifaConsulta t = new TarifaConsulta();
            t.setIdTarifa(parseInt(request.getParameter("idTarifa")));
            t.setIdEspecialidad(parseInt(request.getParameter("idEspecialidad")));
            t.setNombreTarifa(texto(request.getParameter("nombreTarifa")));
            t.setMonto(new BigDecimal(texto(request.getParameter("monto"))));
            t.setMoneda(texto(request.getParameter("moneda")));
            t.setVigenciaDesde(texto(request.getParameter("vigenciaDesde")));
            t.setVigenciaHasta(texto(request.getParameter("vigenciaHasta")));
            t.setEstadoRegistro(texto(request.getParameter("estadoRegistro")));

            ApiClient.post("/tarifas?usuario=" + URLEncoder.encode(usuario, StandardCharsets.UTF_8), t, TarifaConsulta.class);
            response.sendRedirect(request.getContextPath()+"/tarifas?msg=saved");
        } catch (Exception ex) {
            AppLogger.warning(getClass(), "Error guardando tarifa vía REST API", ex);
            response.sendRedirect(request.getContextPath()+"/tarifas?msg=invalid");
        }
    }

    private String usuarioSesion(HttpServletRequest request) { HttpSession s=request.getSession(false); return s==null?"sistema":String.valueOf(s.getAttribute("usuarioLogeado")); }
    private String texto(String v) { return v == null ? "" : v.trim(); }
    private int parseInt(String v) { try { return Integer.parseInt(v); } catch(Exception e) { return 0; } }
}
