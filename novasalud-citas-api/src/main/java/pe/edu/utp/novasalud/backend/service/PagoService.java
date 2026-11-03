package pe.edu.utp.novasalud.backend.service;

import pe.edu.utp.novasalud.backend.dao.PagoDAO;
import pe.edu.utp.novasalud.backend.model.Pago;
import pe.edu.utp.novasalud.backend.model.ReporteFinanciero;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Reglas de negocio de pagos y caja. */
public class PagoService {
    private static final Set<String> METODOS_CON_OPERACION = new HashSet<>(Arrays.asList("YAPE", "PLIN", "TARJETA", "TRANSFERENCIA"));
    private final PagoDAO pagoDAO = new PagoDAO();

    public List<Pago> listar(String fechaInicio, String fechaFin, String estadoPago) throws SQLException { return pagoDAO.listar(fechaInicio, fechaFin, estadoPago); }
    public List<Pago> listar(String fechaInicio, String fechaFin, String estadoPago, String dniPaciente) throws SQLException { return pagoDAO.listar(fechaInicio, fechaFin, estadoPago, dniPaciente); }
    public Pago obtenerPorId(int idPago) throws SQLException { return pagoDAO.obtenerPorId(idPago); }

    public String registrarPago(int idPago, BigDecimal monto, String metodo, String operacion, String observacion, int idUsuario) throws SQLException {
        if (idPago <= 0) throw new IllegalArgumentException("Pago inválido.");
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("El monto debe ser mayor a cero.");
        String metodoNormalizado = metodo == null ? "" : metodo.trim().toUpperCase();
        if (metodoNormalizado.isBlank()) throw new IllegalArgumentException("Seleccione un método de pago.");
        if (METODOS_CON_OPERACION.contains(metodoNormalizado) && (operacion == null || operacion.isBlank())) {
            throw new IllegalArgumentException("El número de operación es obligatorio para " + metodoNormalizado + ".");
        }
        Pago pagoActual = pagoDAO.obtenerPorId(idPago);
        if (pagoActual == null) throw new IllegalArgumentException("Pago no encontrado.");
        if ("PAGADO".equalsIgnoreCase(pagoActual.getEstadoPago()) || "ANULADO".equalsIgnoreCase(pagoActual.getEstadoPago())) {
            throw new IllegalArgumentException("El pago ya no admite nuevos cobros.");
        }
        if (monto.compareTo(pagoActual.getSaldoPendiente()) > 0) {
            throw new IllegalArgumentException("El monto ingresado excede el saldo pendiente.");
        }
        pagoDAO.registrarPago(idPago, monto, metodoNormalizado, operacion, observacion, idUsuario);
        Pago actualizado = pagoDAO.obtenerPorId(idPago);
        return actualizado != null && "PAGADO".equalsIgnoreCase(actualizado.getEstadoPago()) ? "paid" : "partial";
    }

    public void anularPago(int idPago, String motivo) throws SQLException {
        if (idPago <= 0) throw new IllegalArgumentException("Pago inválido.");
        if (motivo == null || motivo.isBlank()) throw new IllegalArgumentException("Debe ingresar motivo de anulación.");
        pagoDAO.anularPago(idPago, motivo);
    }

    public BigDecimal totalIngresos(String inicio, String fin) throws SQLException { return pagoDAO.totalIngresos(inicio, fin); }
    public List<ReporteFinanciero> ingresosPorEspecialidad(String inicio, String fin) throws SQLException { return pagoDAO.ingresosPorEspecialidad(inicio, fin); }
    public List<ReporteFinanciero> ingresosPorMetodoPago(String inicio, String fin) throws SQLException { return pagoDAO.ingresosPorMetodoPago(inicio, fin); }
}
