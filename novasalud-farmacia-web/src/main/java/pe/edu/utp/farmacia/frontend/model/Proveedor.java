package pe.edu.utp.farmacia.frontend.model;

public class Proveedor {
    private int idProveedor;
    private String razonSocial;
    private String ruc;
    private String contacto;
    private String telefono;
    private String correo;
    private String email;
    private String direccion;
    private String estadoRegistro;

    public Proveedor() {
    }

    public int getIdProveedor() { return idProveedor; }
    public void setIdProveedor(int idProveedor) { this.idProveedor = idProveedor; }

    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }

    public String getRuc() { return ruc; }
    public void setRuc(String ruc) { this.ruc = ruc; }

    public String getContacto() { return contacto != null ? contacto : razonSocial; }
    public void setContacto(String contacto) { this.contacto = contacto; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo != null ? correo : email; }
    public void setCorreo(String correo) { this.correo = correo; this.email = correo; }

    public String getEmail() { return email != null ? email : correo; }
    public void setEmail(String email) { this.email = email; this.correo = email; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public boolean isEstado() { return estadoRegistro == null || "ACTIVO".equalsIgnoreCase(estadoRegistro); }

    public String getEstadoRegistro() { return estadoRegistro; }
    public void setEstadoRegistro(String estadoRegistro) { this.estadoRegistro = estadoRegistro; }
}
