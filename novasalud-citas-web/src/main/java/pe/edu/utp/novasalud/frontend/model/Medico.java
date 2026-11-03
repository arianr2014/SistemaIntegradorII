package pe.edu.utp.novasalud.frontend.model;

/** Modelo de médico con parámetros operativos de agenda clínica. */
public class Medico {

    private int idMedico;
    private String nombres;
    private String apellidos;
    private int idEspecialidad;
    private String nombreEspecialidad;
    private String dni;
    private String telefono;
    private String correo;
    private String cmp;
    private int duracionCitaMinutos = 30;
    private int toleranciaMinutos = 10;
    private int maxCitasDia = 12;
    private String consultorio;

    public Medico() {}

    public Medico(int idMedico, String nombres, String apellidos, int idEspecialidad, String nombreEspecialidad, String telefono) {
        this.idMedico = idMedico;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.idEspecialidad = idEspecialidad;
        this.nombreEspecialidad = nombreEspecialidad;
        this.telefono = telefono;
    }

    public int getIdMedico() { return idMedico; }
    public void setIdMedico(int idMedico) { this.idMedico = idMedico; }
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public int getIdEspecialidad() { return idEspecialidad; }
    public void setIdEspecialidad(int idEspecialidad) { this.idEspecialidad = idEspecialidad; }
    public String getNombreEspecialidad() { return nombreEspecialidad; }
    public void setNombreEspecialidad(String nombreEspecialidad) { this.nombreEspecialidad = nombreEspecialidad; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getCmp() { return cmp; }
    public void setCmp(String cmp) { this.cmp = cmp; }
    public int getDuracionCitaMinutos() { return duracionCitaMinutos; }
    public void setDuracionCitaMinutos(int duracionCitaMinutos) { this.duracionCitaMinutos = duracionCitaMinutos; }
    public int getToleranciaMinutos() { return toleranciaMinutos; }
    public void setToleranciaMinutos(int toleranciaMinutos) { this.toleranciaMinutos = toleranciaMinutos; }
    public int getMaxCitasDia() { return maxCitasDia; }
    public void setMaxCitasDia(int maxCitasDia) { this.maxCitasDia = maxCitasDia; }
    public String getConsultorio() { return consultorio; }
    public void setConsultorio(String consultorio) { this.consultorio = consultorio; }

    public String getNombreCompleto() {
        String nom = nombres == null ? "" : nombres;
        String ape = apellidos == null ? "" : apellidos;
        return (nom + " " + ape).trim();
    }
}
