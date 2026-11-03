package pe.edu.utp.farmacia.frontend.model;

import java.math.BigDecimal;

public class Medicamento {
    private int idMedicamento;
    private String codigo;
    private String codigoBarras;
    private String nombre;
    private String nombreComercial;
    private String principioActivo;
    private String nombreGenerico;
    private String concentracion;
    private String formaFarmaceutica;
    private String presentacion;
    private int idCategoria;
    private String nombreCategoria;
    private BigDecimal precioVenta;
    private boolean requiereReceta;
    private String estadoRegistro;

    public Medicamento() {
    }

    public int getIdMedicamento() { return idMedicamento; }
    public void setIdMedicamento(int idMedicamento) { this.idMedicamento = idMedicamento; }

    public String getCodigo() { return codigo != null ? codigo : codigoBarras; }
    public void setCodigo(String codigo) { this.codigo = codigo; this.codigoBarras = codigo; }

    public String getCodigoBarras() { return codigoBarras != null ? codigoBarras : codigo; }
    public void setCodigoBarras(String codigoBarras) { this.codigoBarras = codigoBarras; this.codigo = codigoBarras; }

    public String getNombre() { return nombre != null ? nombre : nombreComercial; }
    public void setNombre(String nombre) { this.nombre = nombre; this.nombreComercial = nombre; }

    public String getNombreComercial() { return nombreComercial != null ? nombreComercial : nombre; }
    public void setNombreComercial(String nombreComercial) { this.nombreComercial = nombreComercial; this.nombre = nombreComercial; }

    public String getPrincipioActivo() { return principioActivo != null ? principioActivo : nombreGenerico; }
    public void setPrincipioActivo(String principioActivo) { this.principioActivo = principioActivo; this.nombreGenerico = principioActivo; }

    public String getNombreGenerico() { return nombreGenerico != null ? nombreGenerico : principioActivo; }
    public void setNombreGenerico(String nombreGenerico) { this.nombreGenerico = nombreGenerico; this.principioActivo = nombreGenerico; }

    public String getConcentracion() { return concentracion; }
    public void setConcentracion(String concentracion) { this.concentracion = concentracion; }

    public String getFormaFarmaceutica() { return formaFarmaceutica; }
    public void setFormaFarmaceutica(String formaFarmaceutica) { this.formaFarmaceutica = formaFarmaceutica; }

    public String getPresentacion() { return presentacion; }
    public void setPresentacion(String presentacion) { this.presentacion = presentacion; }

    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }

    public String getNombreCategoria() { return nombreCategoria; }
    public void setNombreCategoria(String nombreCategoria) { this.nombreCategoria = nombreCategoria; }

    public BigDecimal getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(BigDecimal precioVenta) { this.precioVenta = precioVenta; }

    public boolean isRequiereReceta() { return requiereReceta; }
    public void setRequiereReceta(boolean requiereReceta) { this.requiereReceta = requiereReceta; }

    public boolean isEstado() { return estadoRegistro == null || "ACTIVO".equalsIgnoreCase(estadoRegistro); }

    public String getEstadoRegistro() { return estadoRegistro; }
    public void setEstadoRegistro(String estadoRegistro) { this.estadoRegistro = estadoRegistro; }
}
