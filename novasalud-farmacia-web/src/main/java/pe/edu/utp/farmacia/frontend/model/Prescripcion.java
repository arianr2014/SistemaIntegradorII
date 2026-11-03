package pe.edu.utp.farmacia.frontend.model;

import java.util.List;

public class Prescripcion {
    private int idPrescripcion;
    private String codigoPrescripcion;
    private Integer idPacienteExterno;
    private Integer idMedicoExterno;
    private Integer idAtencionExterna;
    private String fechaPrescripcion;
    private String instruccionesGenerales;
    private String estado;
    private String fechaRecepcion;
    private String estadoRegistro;
    private List<PrescripcionDetalle> detalles;

    // Campos presentacionales para la vista
    private String nombreMedico;
    private String numeroColegiaturaMedico;
    private String nombrePaciente;
    private String dniPaciente;
    private String diagnostico;
    private String fechaEmision;
    private String fechaVencimiento;

    public Prescripcion() {
    }

    public int getIdPrescripcion() { return idPrescripcion; }
    public void setIdPrescripcion(int idPrescripcion) { this.idPrescripcion = idPrescripcion; }
    public String getCodigoPrescripcion() { return codigoPrescripcion; }
    public void setCodigoPrescripcion(String codigoPrescripcion) { this.codigoPrescripcion = codigoPrescripcion; }
    public Integer getIdPacienteExterno() { return idPacienteExterno; }
    public void setIdPacienteExterno(Integer idPacienteExterno) { this.idPacienteExterno = idPacienteExterno; }
    public Integer getIdMedicoExterno() { return idMedicoExterno; }
    public void setIdMedicoExterno(Integer idMedicoExterno) { this.idMedicoExterno = idMedicoExterno; }
    public Integer getIdAtencionExterna() { return idAtencionExterna; }
    public void setIdAtencionExterna(Integer idAtencionExterna) { this.idAtencionExterna = idAtencionExterna; }
    public String getFechaPrescripcion() { return fechaPrescripcion; }
    public void setFechaPrescripcion(String fechaPrescripcion) { this.fechaPrescripcion = fechaPrescripcion; }
    public String getInstruccionesGenerales() { return instruccionesGenerales; }
    public void setInstruccionesGenerales(String instruccionesGenerales) { this.instruccionesGenerales = instruccionesGenerales; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getFechaRecepcion() { return fechaRecepcion; }
    public void setFechaRecepcion(String fechaRecepcion) { this.fechaRecepcion = fechaRecepcion; }
    public String getEstadoRegistro() { return estadoRegistro; }
    public void setEstadoRegistro(String estadoRegistro) { this.estadoRegistro = estadoRegistro; }
    public List<PrescripcionDetalle> getDetalles() { return detalles; }
    public void setDetalles(List<PrescripcionDetalle> detalles) { this.detalles = detalles; }

    public String getNombreMedico() {
        if (nombreMedico != null && !nombreMedico.isBlank()) return nombreMedico;
        return (idMedicoExterno != null) ? "Médico Ext. #" + idMedicoExterno : "Dr. Médico Tratante";
    }
    public void setNombreMedico(String nombreMedico) { this.nombreMedico = nombreMedico; }

    public String getNumeroColegiaturaMedico() {
        if (numeroColegiaturaMedico != null && !numeroColegiaturaMedico.isBlank()) return numeroColegiaturaMedico;
        return (idMedicoExterno != null) ? "CMP-00" + idMedicoExterno : "CMP-45892";
    }
    public void setNumeroColegiaturaMedico(String numeroColegiaturaMedico) { this.numeroColegiaturaMedico = numeroColegiaturaMedico; }

    public String getNombrePaciente() {
        if (nombrePaciente != null && !nombrePaciente.isBlank()) return nombrePaciente;
        return (idPacienteExterno != null) ? "Paciente Ext. #" + idPacienteExterno : "Paciente Registrado";
    }
    public void setNombrePaciente(String nombrePaciente) { this.nombrePaciente = nombrePaciente; }

    public String getDniPaciente() {
        if (dniPaciente != null && !dniPaciente.isBlank()) return dniPaciente;
        return (idPacienteExterno != null) ? "7" + String.format("%07d", idPacienteExterno) : "70123456";
    }
    public void setDniPaciente(String dniPaciente) { this.dniPaciente = dniPaciente; }

    public String getDiagnostico() {
        if (diagnostico != null && !diagnostico.isBlank()) return diagnostico;
        return (instruccionesGenerales != null && !instruccionesGenerales.isBlank()) ? instruccionesGenerales : "Tratamiento médico general";
    }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }

    public String getFechaEmision() {
        if (fechaEmision != null && !fechaEmision.isBlank()) return fechaEmision;
        return (fechaPrescripcion != null) ? fechaPrescripcion : "2026-09-05";
    }
    public void setFechaEmision(String fechaEmision) { this.fechaEmision = fechaEmision; }

    public String getFechaVencimiento() {
        if (fechaVencimiento != null && !fechaVencimiento.isBlank()) return fechaVencimiento;
        return "2026-09-12";
    }
    public void setFechaVencimiento(String fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }
}
