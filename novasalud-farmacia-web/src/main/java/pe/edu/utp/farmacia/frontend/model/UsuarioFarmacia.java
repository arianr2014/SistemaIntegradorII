package pe.edu.utp.farmacia.frontend.model;

public class UsuarioFarmacia {
    private int idUsuario;
    private String username;
    private String nombres;
    private String apellidos;
    private String email;
    private String rol;
    private String estadoRegistro;
    private Boolean estado;

    public UsuarioFarmacia() {
    }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    
    public String getEmail() {
        if (email != null && !email.isBlank()) return email;
        return (username != null && !username.isBlank()) ? username + "@novasalud.pe" : "usuario@novasalud.pe";
    }
    public void setEmail(String email) { this.email = email; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public String getEstadoRegistro() { return estadoRegistro; }
    public void setEstadoRegistro(String estadoRegistro) { this.estadoRegistro = estadoRegistro; }

    public boolean isEstado() {
        if (estado != null) return estado;
        return estadoRegistro == null || "ACTIVO".equalsIgnoreCase(estadoRegistro);
    }
    public boolean getEstado() {
        return isEstado();
    }
    public void setEstado(Boolean estado) { this.estado = estado; }
}
