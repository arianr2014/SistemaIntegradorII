package pe.edu.utp.farmacia.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.sql.Timestamp;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Prescripcion {

    private int idPrescripcion;
    private String codigoPrescripcion;
    private Integer idPacienteExterno;
    private Integer idMedicoExterno;
    private Integer idAtencionExterna;
    private Timestamp fechaPrescripcion;
    private String instruccionesGenerales;
    private String estado;
    private Timestamp fechaRecepcion;
    private String usuarioRegistro;
    private Timestamp fechaRegistro;
    private String usuarioActualizacion;
    private Timestamp fechaActualizacion;
    private String estadoRegistro;
    private List<PrescripcionDetalle> detalles;

    // Campos adicionales para transferencia JSON / UI
    private String nombreMedico;
    private String numeroColegiaturaMedico;
    private String nombrePaciente;
    private String dniPaciente;
    private String diagnostico;
    private String fechaEmision;
    private String fechaVencimiento;

    public Prescripcion() {
    }

    public int getIdPrescripcion() {
        return idPrescripcion;
    }

    public void setIdPrescripcion(int idPrescripcion) {
        this.idPrescripcion = idPrescripcion;
    }

    public String getCodigoPrescripcion() {
        return codigoPrescripcion;
    }

    public void setCodigoPrescripcion(String codigoPrescripcion) {
        this.codigoPrescripcion = codigoPrescripcion;
    }

    public Integer getIdPacienteExterno() {
        return idPacienteExterno;
    }

    public void setIdPacienteExterno(Integer idPacienteExterno) {
        this.idPacienteExterno = idPacienteExterno;
    }

    public Integer getIdMedicoExterno() {
        return idMedicoExterno;
    }

    public void setIdMedicoExterno(Integer idMedicoExterno) {
        this.idMedicoExterno = idMedicoExterno;
    }

    public Integer getIdAtencionExterna() {
        return idAtencionExterna;
    }

    public void setIdAtencionExterna(Integer idAtencionExterna) {
        this.idAtencionExterna = idAtencionExterna;
    }

    public Timestamp getFechaPrescripcion() {
        return fechaPrescripcion;
    }

    public void setFechaPrescripcion(Timestamp fechaPrescripcion) {
        this.fechaPrescripcion = fechaPrescripcion;
    }

    public String getInstruccionesGenerales() {
        return instruccionesGenerales;
    }

    public void setInstruccionesGenerales(String instruccionesGenerales) {
        this.instruccionesGenerales = instruccionesGenerales;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Timestamp getFechaRecepcion() {
        return fechaRecepcion;
    }

    public void setFechaRecepcion(Timestamp fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
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

    public List<PrescripcionDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<PrescripcionDetalle> detalles) {
        this.detalles = detalles;
    }

    public String getNombreMedico() {
        return nombreMedico;
    }

    public void setNombreMedico(String nombreMedico) {
        this.nombreMedico = nombreMedico;
    }

    public String getNumeroColegiaturaMedico() {
        return numeroColegiaturaMedico;
    }

    public void setNumeroColegiaturaMedico(String numeroColegiaturaMedico) {
        this.numeroColegiaturaMedico = numeroColegiaturaMedico;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public String getDniPaciente() {
        return dniPaciente;
    }

    public void setDniPaciente(String dniPaciente) {
        this.dniPaciente = dniPaciente;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(String fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
}
