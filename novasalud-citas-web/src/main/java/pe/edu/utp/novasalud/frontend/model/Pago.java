package pe.edu.utp.novasalud.frontend.model;

import java.math.BigDecimal;

/** Modelo de pago/caja asociado a una cita médica. */
public class Pago {
    private int idPago;
    private int idCita;
    private String codigoPago;
    private String paciente;
    private String pacienteDni;
    private String medico;
    private String especialidad;
    private String fechaCita;
    private String horaCita;
    private BigDecimal montoSubtotal;
    private BigDecimal montoIgv;
    private BigDecimal montoTotal;
    private BigDecimal montoPagado;
    private String estadoPago;
    private String metodoPago;
    private String numeroOperacion;
    private String metodosPago;
    private String operacionesExternas;
    private String fechaPago;
    private String usuarioCobro;
    private String observacion;

    public int getIdPago() { return idPago; }
    public void setIdPago(int idPago) { this.idPago = idPago; }
    public int getIdCita() { return idCita; }
    public void setIdCita(int idCita) { this.idCita = idCita; }
    public String getCodigoPago() { return codigoPago; }
    public void setCodigoPago(String codigoPago) { this.codigoPago = codigoPago; }
    public String getPaciente() { return paciente; }
    public void setPaciente(String paciente) { this.paciente = paciente; }
    public String getPacienteDni() { return pacienteDni; }
    public void setPacienteDni(String pacienteDni) { this.pacienteDni = pacienteDni; }
    public String getMedico() { return medico; }
    public void setMedico(String medico) { this.medico = medico; }
    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
    public String getFechaCita() { return fechaCita; }
    public void setFechaCita(String fechaCita) { this.fechaCita = fechaCita; }
    public String getHoraCita() { return horaCita; }
    public void setHoraCita(String horaCita) { this.horaCita = horaCita; }
    public BigDecimal getMontoSubtotal() { return montoSubtotal; }
    public void setMontoSubtotal(BigDecimal montoSubtotal) { this.montoSubtotal = montoSubtotal; }
    public BigDecimal getMontoIgv() { return montoIgv; }
    public void setMontoIgv(BigDecimal montoIgv) { this.montoIgv = montoIgv; }
    public BigDecimal getMontoTotal() { return montoTotal; }
    public void setMontoTotal(BigDecimal montoTotal) { this.montoTotal = montoTotal; }
    public BigDecimal getMontoPagado() { return montoPagado; }
    public void setMontoPagado(BigDecimal montoPagado) { this.montoPagado = montoPagado; }
    public BigDecimal getSaldoPendiente() {
        BigDecimal total = montoTotal == null ? BigDecimal.ZERO : montoTotal;
        BigDecimal pagado = montoPagado == null ? BigDecimal.ZERO : montoPagado;
        return total.subtract(pagado).max(BigDecimal.ZERO);
    }
    public String getEstadoPago() { return estadoPago; }
    public void setEstadoPago(String estadoPago) { this.estadoPago = estadoPago; }
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    public String getNumeroOperacion() { return numeroOperacion; }
    public void setNumeroOperacion(String numeroOperacion) { this.numeroOperacion = numeroOperacion; }
    public String getMetodosPago() { return metodosPago; }
    public void setMetodosPago(String metodosPago) { this.metodosPago = metodosPago; }
    public String getOperacionesExternas() { return operacionesExternas; }
    public void setOperacionesExternas(String operacionesExternas) { this.operacionesExternas = operacionesExternas; }
    public String getFechaPago() { return fechaPago; }
    public void setFechaPago(String fechaPago) { this.fechaPago = fechaPago; }
    public String getUsuarioCobro() { return usuarioCobro; }
    public void setUsuarioCobro(String usuarioCobro) { this.usuarioCobro = usuarioCobro; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
}
