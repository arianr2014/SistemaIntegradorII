package pe.edu.utp.farmacia.frontend.model;

public class Inventario {
    private int idInventario;
    private int idMedicamento;
    private String codigoMedicamento;
    private String nombreMedicamento;
    private Integer stockActual;
    private Integer stockMinimo;
    private Integer stockMaximo;
    private String ubicacion;
    private String estadoRegistro;

    public Inventario() {
    }

    public int getIdInventario() { return idInventario; }
    public void setIdInventario(int idInventario) { this.idInventario = idInventario; }

    public int getIdMedicamento() { return idMedicamento; }
    public void setIdMedicamento(int idMedicamento) { this.idMedicamento = idMedicamento; }

    public String getCodigoMedicamento() { return codigoMedicamento; }
    public void setCodigoMedicamento(String codigoMedicamento) { this.codigoMedicamento = codigoMedicamento; }

    public String getNombreMedicamento() { return nombreMedicamento; }
    public void setNombreMedicamento(String nombreMedicamento) { this.nombreMedicamento = nombreMedicamento; }

    public Integer getStockActual() { return stockActual != null ? stockActual : 0; }
    public void setStockActual(Integer stockActual) { this.stockActual = stockActual; }

    public Integer getStockMinimo() { return stockMinimo != null ? stockMinimo : 0; }
    public void setStockMinimo(Integer stockMinimo) { this.stockMinimo = stockMinimo; }

    public Integer getStockMaximo() { return stockMaximo != null ? stockMaximo : 0; }
    public void setStockMaximo(Integer stockMaximo) { this.stockMaximo = stockMaximo; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public String getEstadoRegistro() { return estadoRegistro; }
    public void setEstadoRegistro(String estadoRegistro) { this.estadoRegistro = estadoRegistro; }
}
