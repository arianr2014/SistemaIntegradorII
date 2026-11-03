package pe.edu.utp.farmacia.frontend.model;

public class CategoriaMedicamento {
    private int idCategoria;
    private String nombre;
    private String nombreCategoria;
    private String descripcion;
    private boolean requiereReceta;
    private String estadoRegistro;

    public CategoriaMedicamento() {
    }

    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }

    public String getNombre() { return nombre != null ? nombre : nombreCategoria; }
    public void setNombre(String nombre) { this.nombre = nombre; this.nombreCategoria = nombre; }

    public String getNombreCategoria() { return nombreCategoria != null ? nombreCategoria : nombre; }
    public void setNombreCategoria(String nombreCategoria) { this.nombreCategoria = nombreCategoria; this.nombre = nombreCategoria; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public boolean isRequiereReceta() { return requiereReceta; }
    public void setRequiereReceta(boolean requiereReceta) { this.requiereReceta = requiereReceta; }

    public boolean isEstado() { return estadoRegistro == null || "ACTIVO".equalsIgnoreCase(estadoRegistro); }

    public String getEstadoRegistro() { return estadoRegistro; }
    public void setEstadoRegistro(String estadoRegistro) { this.estadoRegistro = estadoRegistro; }
}
