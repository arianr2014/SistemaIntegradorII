package pe.edu.utp.farmacia.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Venta {

    private int idVenta;
    private String codigoVenta;
    private String dni;
    private String nombres;
    private String apellidos;
    private Integer idPrescripcion;
    private Timestamp fechaVenta;
    private BigDecimal subtotal;
    private BigDecimal igv;
    private BigDecimal total;
    private String metodoPago;
    private Integer idUsuario;
    private String usernameUsuario;
    private String usuarioRegistro;
    private Timestamp fechaRegistro;
    private String usuarioActualizacion;
    private Timestamp fechaActualizacion;
    private String estadoRegistro;
    private String tipoComprobante;
    private List<VentaDetalle> detalles;

    public Venta() {
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public String getCodigoVenta() {
        return codigoVenta;
    }

    public void setCodigoVenta(String codigoVenta) {
        this.codigoVenta = codigoVenta;
    }

    public String getNumeroBoleta() {
        return codigoVenta;
    }

    public void setNumeroBoleta(String numeroBoleta) {
        if (this.codigoVenta == null || this.codigoVenta.isBlank()) {
            this.codigoVenta = numeroBoleta;
        }
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getDniCliente() {
        return dni;
    }

    public void setDniCliente(String dniCliente) {
        if (this.dni == null || this.dni.isBlank()) {
            this.dni = dniCliente;
        }
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getNombreCliente() {
        return nombres;
    }

    public void setNombreCliente(String nombreCliente) {
        if (this.nombres == null || this.nombres.isBlank()) {
            this.nombres = nombreCliente;
        }
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Integer getIdPrescripcion() {
        return idPrescripcion;
    }

    public void setIdPrescripcion(Integer idPrescripcion) {
        this.idPrescripcion = idPrescripcion;
    }

    public Timestamp getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(Timestamp fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getIgv() {
        return igv;
    }

    public void setIgv(BigDecimal igv) {
        this.igv = igv;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getMontoTotal() {
        return total;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        if (this.total == null) {
            this.total = montoTotal;
        }
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getTipoPago() {
        return metodoPago;
    }

    public void setTipoPago(String tipoPago) {
        if (this.metodoPago == null || this.metodoPago.isBlank()) {
            this.metodoPago = tipoPago;
        }
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsernameUsuario() {
        return usernameUsuario;
    }

    public void setUsernameUsuario(String usernameUsuario) {
        this.usernameUsuario = usernameUsuario;
    }

    public String getUsuarioRegistro() {
        return usuarioRegistro;
    }

    public void setUsuarioRegistro(String usuarioRegistro) {
        this.usuarioRegistro = usuarioRegistro;
    }

    public Timestamp getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Timestamp fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getUsuarioActualizacion() {
        return usuarioActualizacion;
    }

    public void setUsuarioActualizacion(String usuarioActualizacion) {
        this.usuarioActualizacion = usuarioActualizacion;
    }

    public Timestamp getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Timestamp fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public String getEstadoRegistro() {
        return estadoRegistro;
    }

    public void setEstadoRegistro(String estadoRegistro) {
        this.estadoRegistro = estadoRegistro;
    }

    public String getTipoComprobante() {
        if (tipoComprobante != null && !tipoComprobante.isBlank()) return tipoComprobante;
        String code = getCodigoVenta();
        if (code != null && code.startsWith("F")) return "FACTURA ELECTRÓNICA";
        return "BOLETA ELECTRÓNICA";
    }

    public void setTipoComprobante(String tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

    public List<VentaDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<VentaDetalle> detalles) {
        this.detalles = detalles;
    }
}
