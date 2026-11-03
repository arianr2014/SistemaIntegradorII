package pe.edu.utp.farmacia.frontend.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import pe.edu.utp.farmacia.frontend.model.CategoriaMedicamento;
import pe.edu.utp.farmacia.frontend.model.Inventario;
import pe.edu.utp.farmacia.frontend.model.LoteMedicamento;
import pe.edu.utp.farmacia.frontend.model.Medicamento;
import pe.edu.utp.farmacia.frontend.model.Venta;
import pe.edu.utp.farmacia.frontend.util.ApiClient;
import pe.edu.utp.farmacia.frontend.util.AppLogger;
import pe.edu.utp.farmacia.frontend.util.JwtTokenContext;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

@WebServlet({"/home", "/dashboard"})
public class HomeController extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String token = (session != null) ? (String) session.getAttribute("jwtToken") : null;
        JwtTokenContext.setToken(token);

        try {
            ApiClient.ApiResponse medResp = ApiClient.get("/medicamentos", token);
            ApiClient.ApiResponse catResp = ApiClient.get("/categorias", token);
            ApiClient.ApiResponse ventaResp = ApiClient.get("/ventas", token);
            ApiClient.ApiResponse invResp = ApiClient.get("/inventario", token);
            ApiClient.ApiResponse loteResp = ApiClient.get("/lotes", token);

            if (medResp.getStatusCode() == 200) {
                Type listType = new TypeToken<List<Medicamento>>() {}.getType();
                List<Medicamento> medicamentos = gson.fromJson(medResp.getBody(), listType);
                request.setAttribute("totalMedicamentos", medicamentos.size());
                request.setAttribute("recientesMedicamentos", medicamentos.stream().limit(5).toList());
            }

            if (catResp.getStatusCode() == 200) {
                Type listType = new TypeToken<List<CategoriaMedicamento>>() {}.getType();
                List<CategoriaMedicamento> categorias = gson.fromJson(catResp.getBody(), listType);
                request.setAttribute("totalCategorias", categorias.size());
            }

            if (ventaResp.getStatusCode() == 200) {
                Type listType = new TypeToken<List<Venta>>() {}.getType();
                List<Venta> ventas = gson.fromJson(ventaResp.getBody(), listType);
                request.setAttribute("totalVentas", ventas.size());
                double totalIngresos = ventas.stream().mapToDouble(v -> v.getMontoTotal() != null ? v.getMontoTotal().doubleValue() : 0.0).sum();
                request.setAttribute("totalIngresos", totalIngresos);
                request.setAttribute("recientesVentas", ventas.stream().limit(5).toList());
            }

            if (invResp.getStatusCode() == 200) {
                Type listType = new TypeToken<List<Inventario>>() {}.getType();
                List<Inventario> inventarios = gson.fromJson(invResp.getBody(), listType);
                long stockBajo = inventarios.stream().filter(i -> i.getStockActual() <= i.getStockMinimo()).count();
                request.setAttribute("stockBajoCount", stockBajo);
            }

            if (loteResp.getStatusCode() == 200) {
                Type listType = new TypeToken<List<LoteMedicamento>>() {}.getType();
                List<LoteMedicamento> lotes = gson.fromJson(loteResp.getBody(), listType);
                request.setAttribute("lotesCount", lotes.size());
            }

        } catch (Exception e) {
            AppLogger.error(HomeController.class, "Error al cargar datos del Dashboard: " + e.getMessage(), e);
        }

        request.getRequestDispatcher("/WEB-INF/views/home/index.jsp").forward(request, response);
    }
}
