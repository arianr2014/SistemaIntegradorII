package pe.edu.utp.novasalud.backend.service;

import pe.edu.utp.novasalud.backend.dao.TarifaConsultaDAO;
import pe.edu.utp.novasalud.backend.model.TarifaConsulta;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/** Reglas de negocio de tarifas por especialidad. */
public class TarifaConsultaService {
    private final TarifaConsultaDAO dao = new TarifaConsultaDAO();
    public List<TarifaConsulta> listar() throws SQLException { return dao.listar(); }
    public TarifaConsulta obtenerPorId(int id) throws SQLException { return dao.obtenerPorId(id); }
    public void guardar(TarifaConsulta tarifa, String usuario) throws SQLException {
        if (tarifa.getIdEspecialidad() <= 0) throw new IllegalArgumentException("Debe seleccionar una especialidad.");
        if (tarifa.getNombreTarifa() == null || tarifa.getNombreTarifa().isBlank()) tarifa.setNombreTarifa("Consulta médica");
        if (tarifa.getMonto() == null || tarifa.getMonto().compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("El monto debe ser mayor a cero.");
        if (tarifa.getMoneda() == null || tarifa.getMoneda().isBlank()) tarifa.setMoneda("PEN");
        if (tarifa.getVigenciaDesde() == null || tarifa.getVigenciaDesde().isBlank()) throw new IllegalArgumentException("La vigencia desde es obligatoria.");
        if (tarifa.getEstadoRegistro() == null || tarifa.getEstadoRegistro().isBlank()) tarifa.setEstadoRegistro("ACTIVO");
        dao.guardar(tarifa, usuario);
    }
    public void desactivar(int id, String usuario) throws SQLException { dao.desactivar(id, usuario); }
}
