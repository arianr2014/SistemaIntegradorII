package pe.edu.utp.farmacia.frontend.model;

public class PrescripcionDetalle {
    private int idDetallePrescripcion;
    private int idPrescripcion;
    private Integer idMedicamento;
    private String nombreMedicamento;
    private int cantidad;
    private String dosis;
    private String frecuencia;
    private String duracion;
    private String indicaciones;

    public PrescripcionDetalle() {
    }

    public int getIdDetallePrescripcion() { return idDetallePrescripcion; }
    public void setIdDetallePrescripcion(int idDetallePrescripcion) { this.idDetallePrescripcion = idDetallePrescripcion; }
    public int getIdPrescripcion() { return idPrescripcion; }
    public void setIdPrescripcion(int idPrescripcion) { this.idPrescripcion = idPrescripcion; }
    public Integer getIdMedicamento() { return idMedicamento; }
    public void setIdMedicamento(Integer idMedicamento) { this.idMedicamento = idMedicamento; }
    public String getNombreMedicamento() { return nombreMedicamento; }
    public void setNombreMedicamento(String nombreMedicamento) { this.nombreMedicamento = nombreMedicamento; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public String getDosis() { return dosis; }
    public void setDosis(String dosis) { this.dosis = dosis; }
    public String getFrecuencia() { return frecuencia; }
    public void setFrecuencia(String frecuencia) { this.frecuencia = frecuencia; }
    public String getDuracion() { return duracion; }
    public void setDuracion(String duracion) { this.duracion = duracion; }
    public String getIndicaciones() { return indicaciones; }
    public void setIndicaciones(String indicaciones) { this.indicaciones = indicaciones; }
}
