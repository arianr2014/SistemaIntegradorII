package pe.edu.utp.novasalud.backend.model;

import java.math.BigDecimal;

/** Modelo de cita médica con trazabilidad V3.2. */
public class Cita {

    private int idCita;
    private int idPaciente;
    private int idMedico;
    private Integer idEspecialidad;
    private Integer idTarifaAplicada;
    private Integer idUsuarioRecepcionista;
    private BigDecimal montoConsulta;
    private String fecha;
    private String hora;
    private Integer duracionMinutos;
    private String horaFin;
    private String estado;
    private String observaciones;

    private String pacienteDni;
    private String historiaClinicaCodigo;
    private String pacienteNombreCompleto;
    private String medicoNombreCompleto;
    private String especialidad;
    private String estadoPago;
    private String montoConsultaTexto;
    private Integer idPago;
    private Integer idAtencion;

    public int getIdCita() { return idCita; }
    public void setIdCita(int idCita) { this.idCita = idCita; }
    public int getIdPaciente() { return idPaciente; }
    public void setIdPaciente(int idPaciente) { this.idPaciente = idPaciente; }
    public int getIdMedico() { return idMedico; }
    public void setIdMedico(int idMedico) { this.idMedico = idMedico; }
    public Integer getIdEspecialidad() { return idEspecialidad; }
    public void setIdEspecialidad(Integer idEspecialidad) { this.idEspecialidad = idEspecialidad; }
    public Integer getIdTarifaAplicada() { return idTarifaAplicada; }
    public void setIdTarifaAplicada(Integer idTarifaAplicada) { this.idTarifaAplicada = idTarifaAplicada; }
    public Integer getIdUsuarioRecepcionista() { return idUsuarioRecepcionista; }
    public void setIdUsuarioRecepcionista(Integer idUsuarioRecepcionista) { this.idUsuarioRecepcionista = idUsuarioRecepcionista; }
    public BigDecimal getMontoConsultaDecimal() { return montoConsulta; }
    public void setMontoConsultaDecimal(BigDecimal montoConsulta) { this.montoConsulta = montoConsulta; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }
    public Integer getDuracionMinutos() { return duracionMinutos; }
    public void setDuracionMinutos(Integer duracionMinutos) { this.duracionMinutos = duracionMinutos; }
    public String getHoraFin() { return horaFin; }
    public void setHoraFin(String horaFin) { this.horaFin = horaFin; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public String getPacienteDni() { return pacienteDni; }
    public void setPacienteDni(String pacienteDni) { this.pacienteDni = pacienteDni; }
    public String getHistoriaClinicaCodigo() { return historiaClinicaCodigo; }
    public void setHistoriaClinicaCodigo(String historiaClinicaCodigo) { this.historiaClinicaCodigo = historiaClinicaCodigo; }
    public String getPacienteNombreCompleto() { return pacienteNombreCompleto; }
    public void setPacienteNombreCompleto(String pacienteNombreCompleto) { this.pacienteNombreCompleto = pacienteNombreCompleto; }
    public String getMedicoNombreCompleto() { return medicoNombreCompleto; }
    public void setMedicoNombreCompleto(String medicoNombreCompleto) { this.medicoNombreCompleto = medicoNombreCompleto; }
    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
    public String getEstadoPago() { return estadoPago; }
    public void setEstadoPago(String estadoPago) { this.estadoPago = estadoPago; }

    /** Mantiene compatibilidad con vistas existentes que usan cita.montoConsulta. */
    public String getMontoConsulta() { return montoConsultaTexto; }
    public void setMontoConsulta(String montoConsultaTexto) { this.montoConsultaTexto = montoConsultaTexto; }

    public Integer getIdPago() { return idPago; }
    public void setIdPago(Integer idPago) { this.idPago = idPago; }
    public Integer getIdAtencion() { return idAtencion; }
    public void setIdAtencion(Integer idAtencion) { this.idAtencion = idAtencion; }
}
