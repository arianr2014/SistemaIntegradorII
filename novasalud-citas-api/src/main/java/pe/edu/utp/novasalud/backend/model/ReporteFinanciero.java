package pe.edu.utp.novasalud.backend.model;

import java.math.BigDecimal;

/** DTO para reportes financieros de caja. */
public class ReporteFinanciero {
    private String etiqueta;
    private int cantidad;
    private BigDecimal total;

    public String getEtiqueta() { return etiqueta; }
    public void setEtiqueta(String etiqueta) { this.etiqueta = etiqueta; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
}
