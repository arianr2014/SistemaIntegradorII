package pe.edu.utp.novasalud.backend.model;

/**
 * Registro clínico de una atención médica asociada a una cita.
 * La historia clínica pertenece al paciente y cada cita atendida puede generar
 * o actualizar un registro de síntomas, diagnóstico, tratamiento y receta.
 */
public class AtencionMedica {

    private int idAtencion;
    private int idCita;
    private int idMedico;
    private int idPaciente;
    private String codigoHistoria;
    private String motivoConsulta;
    private String sintomas;
    private String diagnostico;
    private String tratamiento;
    private String recetaMedica;
    private String indicaciones;
    private String fechaAtencion;
    private String nombreMedico;
    private String especialidadNombre;
    private String nombrePaciente;

    public int getIdAtencion() { return idAtencion; }
    public void setIdAtencion(int idAtencion) { this.idAtencion = idAtencion; }

    public int getIdCita() { return idCita; }
    public void setIdCita(int idCita) { this.idCita = idCita; }

    public int getIdMedico() { return idMedico; }
    public void setIdMedico(int idMedico) { this.idMedico = idMedico; }

    public int getIdPaciente() { return idPaciente; }
    public void setIdPaciente(int idPaciente) { this.idPaciente = idPaciente; }

    public String getCodigoHistoria() { return codigoHistoria; }
    public void setCodigoHistoria(String codigoHistoria) { this.codigoHistoria = codigoHistoria; }

    public String getMotivoConsulta() { return motivoConsulta; }
    public void setMotivoConsulta(String motivoConsulta) { this.motivoConsulta = motivoConsulta; }

    public String getSintomas() { return sintomas; }
    public void setSintomas(String sintomas) { this.sintomas = sintomas; }

    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }

    public String getTratamiento() { return tratamiento; }
    public void setTratamiento(String tratamiento) { this.tratamiento = tratamiento; }

    public String getRecetaMedica() { return recetaMedica; }
    public void setRecetaMedica(String recetaMedica) { this.recetaMedica = recetaMedica; }

    public String getIndicaciones() { return indicaciones; }
    public void setIndicaciones(String indicaciones) { this.indicaciones = indicaciones; }

    public String getFechaAtencion() { return fechaAtencion; }
    public void setFechaAtencion(String fechaAtencion) { this.fechaAtencion = fechaAtencion; }

    public String getNombreMedico() { return nombreMedico; }
    public void setNombreMedico(String nombreMedico) { this.nombreMedico = nombreMedico; }

    public String getEspecialidadNombre() { return especialidadNombre; }
    public void setEspecialidadNombre(String especialidadNombre) { this.especialidadNombre = especialidadNombre; }

    public String getNombrePaciente() { return nombrePaciente; }
    public void setNombrePaciente(String nombrePaciente) { this.nombrePaciente = nombrePaciente; }
}
