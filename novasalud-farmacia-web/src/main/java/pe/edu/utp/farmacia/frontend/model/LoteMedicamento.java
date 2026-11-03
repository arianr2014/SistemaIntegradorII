package pe.edu.utp.farmacia.frontend.model;

import java.math.BigDecimal;

public class LoteMedicamento {
    private int idLote;
    private int idMedicamento;
    private String nombreMedicamento;
    private int idProveedor;
    private String razonSocialProveedor;
    private String numeroLote;
    private String fechaFabricacion;
    private String fechaVencimiento;
    private int cantidadInicial;
    private int cantidadDisponible;
    private Integer cantidadActual;
    private BigDecimal precioCompra;
    private String estadoRegistro;

    public LoteMedicamento() {
    }

    public int getIdLote() { return idLote; }
    public void setIdLote(int idLote) { this.idLote = idLote; }

    public int getIdMedicamento() { return idMedicamento; }
    public void setIdMedicamento(int idMedicamento) { this.idMedicamento = idMedicamento; }

    public String getNombreMedicamento() { return nombreMedicamento; }
    public void setNombreMedicamento(String nombreMedicamento) { this.nombreMedicamento = nombreMedicamento; }

    public int getIdProveedor() { return idProveedor; }
    public void setIdProveedor(int idProveedor) { this.idProveedor = idProveedor; }

    public String getRazonSocialProveedor() { return razonSocialProveedor; }
    public void setRazonSocialProveedor(String razonSocialProveedor) { this.razonSocialProveedor = razonSocialProveedor; }

    public String getNumeroLote() { return numeroLote; }
    public void setNumeroLote(String numeroLote) { this.numeroLote = numeroLote; }

    public String getFechaFabricacion() { return fechaFabricacion; }
    public void setFechaFabricacion(String fechaFabricacion) { this.fechaFabricacion = fechaFabricacion; }

    public String getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(String fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }

    public int getCantidadInicial() { return cantidadInicial; }
    public void setCantidadInicial(int cantidadInicial) { this.cantidadInicial = cantidadInicial; }

    public int getCantidadDisponible() { return cantidadDisponible != 0 ? cantidadDisponible : (cantidadActual != null ? cantidadActual : 0); }
    public void setCantidadDisponible(int cantidadDisponible) { this.cantidadDisponible = cantidadDisponible; this.cantidadActual = cantidadDisponible; }

    public int getCantidadActual() { return cantidadActual != null ? cantidadActual : cantidadDisponible; }
    public void setCantidadActual(Integer cantidadActual) { this.cantidadActual = cantidadActual; if (cantidadActual != null) this.cantidadDisponible = cantidadActual; }

    public BigDecimal getPrecioCompra() { return precioCompra != null ? precioCompra : new BigDecimal("10.00"); }
    public void setPrecioCompra(BigDecimal precioCompra) { this.precioCompra = precioCompra; }

    public boolean isEstado() { return estadoRegistro == null || "ACTIVO".equalsIgnoreCase(estadoRegistro); }

    public String getEstadoRegistro() { return estadoRegistro; }
    public void setEstadoRegistro(String estadoRegistro) { this.estadoRegistro = estadoRegistro; }
}
