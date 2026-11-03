package pe.edu.utp.novasalud.backend.model;

/**
 * Modelo de usuario del sistema.
 *
 * V3.2.1:
 * - Un usuario con rol DOCTOR debe estar vinculado a un médico real.
 * - La especialidad se obtiene desde el médico asociado, no desde el login.
 */
public class Usuario {

    private int idUsuario;
    private String username;
    private String password;
    private String rol;
    private String estadoRegistro;
    private String dni;
    private String nombres;
    private String apellidos;
    private String telefono;
    private String correo;
    private String cargo;
    private int sessionVersion;
    private Integer idMedico;
    private String medicoNombreCompleto;
    private Integer idEspecialidad;
    private String especialidadNombre;

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getEstadoRegistro() { return estadoRegistro; }
    public void setEstadoRegistro(String estadoRegistro) { this.estadoRegistro = estadoRegistro; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public int getSessionVersion() { return sessionVersion; }
    public void setSessionVersion(int sessionVersion) { this.sessionVersion = sessionVersion; }

    public String getNombreCompleto() {
        return ((nombres == null ? "" : nombres) + " " + (apellidos == null ? "" : apellidos)).trim();
    }

    public Integer getIdMedico() { return idMedico; }
    public void setIdMedico(Integer idMedico) { this.idMedico = idMedico; }

    public String getMedicoNombreCompleto() { return medicoNombreCompleto; }
    public void setMedicoNombreCompleto(String medicoNombreCompleto) { this.medicoNombreCompleto = medicoNombreCompleto; }

    public Integer getIdEspecialidad() { return idEspecialidad; }
    public void setIdEspecialidad(Integer idEspecialidad) { this.idEspecialidad = idEspecialidad; }

    public String getEspecialidadNombre() { return especialidadNombre; }
    public void setEspecialidadNombre(String especialidadNombre) { this.especialidadNombre = especialidadNombre; }

    public boolean esDoctor() {
        return rol != null && "DOCTOR".equalsIgnoreCase(rol.trim());
    }

    public boolean tieneMedicoAsociado() {
        return idMedico != null && idMedico > 0;
    }
}
