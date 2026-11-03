package pe.edu.utp.novasalud.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HistoriaClinica {

    private int idHistoria;
    private int idPaciente;
    private String codigoHistoria;
    private String antecedentesGenerales;
    private String alergias;
    private String usuarioRegistro;
    private String fechaRegistro;
    private String estadoRegistro;

    // Datos relacionales del paciente
    private Paciente paciente;
    private List<AtencionMedica> atenciones;

    public HistoriaClinica() {
    }

    public int getIdHistoria() {
        return idHistoria;
    }

    public void setIdHistoria(int idHistoria) {
        this.idHistoria = idHistoria;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    public String getCodigoHistoria() {
        return codigoHistoria;
    }

    public void setCodigoHistoria(String codigoHistoria) {
        this.codigoHistoria = codigoHistoria;
    }

    public String getAntecedentesGenerales() {
        return antecedentesGenerales;
    }

    public void setAntecedentesGenerales(String antecedentesGenerales) {
        this.antecedentesGenerales = antecedentesGenerales;
    }

    public String getAlergias() {
        return alergias;
    }

    public void setAlergias(String alergias) {
        this.alergias = alergias;
    }

    public String getUsuarioRegistro() {
        return usuarioRegistro;
    }

    public void setUsuarioRegistro(String usuarioRegistro) {
        this.usuarioRegistro = usuarioRegistro;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getEstadoRegistro() {
        return estadoRegistro;
    }

    public void setEstadoRegistro(String estadoRegistro) {
        this.estadoRegistro = estadoRegistro;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public List<AtencionMedica> getAtenciones() {
        return atenciones;
    }

    public void setAtenciones(List<AtencionMedica> atenciones) {
        this.atenciones = atenciones;
    }
}
