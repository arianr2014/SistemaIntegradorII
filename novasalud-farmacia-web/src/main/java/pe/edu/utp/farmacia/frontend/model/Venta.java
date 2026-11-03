package pe.edu.utp.farmacia.frontend.model;

import java.math.BigDecimal;
import java.util.List;

public class Venta {
    private int idVenta;
    private String numeroBoleta;
    private String codigoVenta;
    private String dniCliente;
    private String dni;
    private String nombreCliente;
    private String nombres;
    private String apellidos;
    private Integer idPrescripcion;
    private String fechaVenta;
    private BigDecimal subtotal;
    private BigDecimal igv;
    private BigDecimal montoTotal;
    private BigDecimal total;
    private String tipoPago;
    private String metodoPago;
    private Integer idUsuario;
    private String usernameUsuario;
    private String estado;
    private String estadoRegistro;
    private String tipoComprobante;
    private List<VentaDetalle> detalles;

    public Venta() {
    }

    public int getIdVenta() { return idVenta; }
    public void setIdVenta(int idVenta) { this.idVenta = idVenta; }

    public String getNumeroBoleta() { return numeroBoleta != null ? numeroBoleta : codigoVenta; }
    public void setNumeroBoleta(String numeroBoleta) { this.numeroBoleta = numeroBoleta; this.codigoVenta = numeroBoleta; }

    public String getCodigoVenta() { return codigoVenta != null ? codigoVenta : numeroBoleta; }
    public void setCodigoVenta(String codigoVenta) { this.codigoVenta = codigoVenta; this.numeroBoleta = codigoVenta; }

    public String getDniCliente() { return dniCliente != null ? dniCliente : dni; }
    public void setDniCliente(String dniCliente) { this.dniCliente = dniCliente; this.dni = dniCliente; }

    public String getDni() { return dni != null ? dni : dniCliente; }
    public void setDni(String dni) { this.dni = dni; this.dniCliente = dni; }

    public String getNombreCliente() { return nombreCliente != null ? nombreCliente : (nombres != null ? nombres + " " + (apellidos != null ? apellidos : "") : ""); }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public Integer getIdPrescripcion() { return idPrescripcion; }
    public void setIdPrescripcion(Integer idPrescripcion) { this.idPrescripcion = idPrescripcion; }

    public String getFechaVenta() { return fechaVenta; }
    public void setFechaVenta(String fechaVenta) { this.fechaVenta = fechaVenta; }

    public String getFechaVentaFormateada() {
        if (fechaVenta == null || fechaVenta.isBlank()) {
            return new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date());
        }
        try {
            String trimmed = fechaVenta.trim();
            if (trimmed.matches("^\\d+$")) {
                long epoch = Long.parseLong(trimmed);
                return new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date(epoch));
            }
        } catch (Exception e) {
            // ignore
        }
        return fechaVenta;
    }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public BigDecimal getIgv() { return igv; }
    public void setIgv(BigDecimal igv) { this.igv = igv; }

    public BigDecimal getMontoTotal() { return montoTotal != null ? montoTotal : total; }
    public void setMontoTotal(BigDecimal montoTotal) { this.montoTotal = montoTotal; this.total = montoTotal; }

    public BigDecimal getTotal() { return total != null ? total : montoTotal; }
    public void setTotal(BigDecimal total) { this.total = total; this.montoTotal = total; }

    public String getTipoPago() { return tipoPago != null ? tipoPago : metodoPago; }
    public void setTipoPago(String tipoPago) { this.tipoPago = tipoPago; this.metodoPago = tipoPago; }

    public String getMetodoPago() { return metodoPago != null ? metodoPago : tipoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; this.tipoPago = metodoPago; }

    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }

    public String getUsernameUsuario() { return usernameUsuario; }
    public void setUsernameUsuario(String usernameUsuario) { this.usernameUsuario = usernameUsuario; }

    public String getEstado() { return estado != null ? estado : estadoRegistro; }
    public void setEstado(String estado) { this.estado = estado; this.estadoRegistro = estado; }

    public String getTipoComprobante() {
        if (tipoComprobante != null && !tipoComprobante.isBlank()) return tipoComprobante;
        String code = getCodigoVenta();
        if (code != null && code.startsWith("F")) return "FACTURA ELECTRÓNICA";
        return "BOLETA ELECTRÓNICA";
    }
    public void setTipoComprobante(String tipoComprobante) { this.tipoComprobante = tipoComprobante; }

    public List<VentaDetalle> getDetalles() { return detalles; }
    public void setDetalles(List<VentaDetalle> detalles) { this.detalles = detalles; }
}
