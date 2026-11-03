package pe.edu.utp.novasalud.backend.model;

import java.math.BigDecimal;

/** Modelo de tarifa por especialidad. */
public class TarifaConsulta {
    private int idTarifa;
    private int idEspecialidad;
    private String especialidad;
    private String nombreTarifa;
    private BigDecimal monto;
    private String moneda;
    private String vigenciaDesde;
    private String vigenciaHasta;
    private String estadoRegistro;

    public int getIdTarifa() { return idTarifa; }
    public void setIdTarifa(int idTarifa) { this.idTarifa = idTarifa; }
    public int getIdEspecialidad() { return idEspecialidad; }
    public void setIdEspecialidad(int idEspecialidad) { this.idEspecialidad = idEspecialidad; }
    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
    public String getNombreTarifa() { return nombreTarifa; }
    public void setNombreTarifa(String nombreTarifa) { this.nombreTarifa = nombreTarifa; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public String getMoneda() { return moneda; }
    public void setMoneda(String moneda) { this.moneda = moneda; }
    public String getVigenciaDesde() { return vigenciaDesde; }
    public void setVigenciaDesde(String vigenciaDesde) { this.vigenciaDesde = vigenciaDesde; }
    public String getVigenciaHasta() { return vigenciaHasta; }
    public void setVigenciaHasta(String vigenciaHasta) { this.vigenciaHasta = vigenciaHasta; }
    public String getEstadoRegistro() { return estadoRegistro; }
    public void setEstadoRegistro(String estadoRegistro) { this.estadoRegistro = estadoRegistro; }
}
