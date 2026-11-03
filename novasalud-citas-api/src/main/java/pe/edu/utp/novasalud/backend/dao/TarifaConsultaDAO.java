package pe.edu.utp.novasalud.backend.dao;

import pe.edu.utp.novasalud.backend.model.TarifaConsulta;
import pe.edu.utp.novasalud.backend.util.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** DAO para tarifas de consulta por especialidad. */
public class TarifaConsultaDAO {

    public List<TarifaConsulta> listar() throws SQLException {
        List<TarifaConsulta> tarifas = new ArrayList<>();
        String sql = "SELECT t.id_tarifa, t.id_especialidad, e.nombre AS especialidad, t.nombre_tarifa, t.monto, "
                + "t.moneda, t.vigencia_desde, t.vigencia_hasta, t.estado_registro "
                + "FROM tarifa_consulta t INNER JOIN especialidad e ON t.id_especialidad = e.id_especialidad "
                + "ORDER BY e.nombre ASC, t.vigencia_desde DESC";
        try (Connection cn = ConexionDB.getConnection(); PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                tarifas.add(mapper(rs));
            }
        }
        return tarifas;
    }

    public TarifaConsulta obtenerPorId(int idTarifa) throws SQLException {
        String sql = "SELECT t.id_tarifa, t.id_especialidad, e.nombre AS especialidad, t.nombre_tarifa, t.monto, "
                + "t.moneda, t.vigencia_desde, t.vigencia_hasta, t.estado_registro "
                + "FROM tarifa_consulta t INNER JOIN especialidad e ON t.id_especialidad = e.id_especialidad WHERE t.id_tarifa = ?";
        try (Connection cn = ConexionDB.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idTarifa);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapper(rs);
            }
        }
        return null;
    }

    public void guardar(TarifaConsulta tarifa, String usuario) throws SQLException {
        if (tarifa.getIdTarifa() > 0) {
            actualizar(tarifa, usuario);
        } else {
            registrar(tarifa, usuario);
        }
    }

    private void registrar(TarifaConsulta tarifa, String usuario) throws SQLException {
        String sql = "INSERT INTO tarifa_consulta(id_especialidad, nombre_tarifa, monto, moneda, vigencia_desde, vigencia_hasta, estado_registro, usuario_registro, fecha_registro) "
                + "VALUES (?, ?, ?, ?, ?, ?, 'ACTIVO', ?, NOW())";
        try (Connection cn = ConexionDB.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, tarifa.getIdEspecialidad());
            ps.setString(2, tarifa.getNombreTarifa());
            ps.setBigDecimal(3, tarifa.getMonto());
            ps.setString(4, tarifa.getMoneda());
            ps.setString(5, tarifa.getVigenciaDesde());
            ps.setString(6, tarifa.getVigenciaHasta() == null || tarifa.getVigenciaHasta().isBlank() ? null : tarifa.getVigenciaHasta());
            ps.setString(7, usuario);
            ps.executeUpdate();
        }
    }

    private void actualizar(TarifaConsulta tarifa, String usuario) throws SQLException {
        String sql = "UPDATE tarifa_consulta SET id_especialidad=?, nombre_tarifa=?, monto=?, moneda=?, vigencia_desde=?, vigencia_hasta=?, estado_registro=?, usuario_actualizacion=?, fecha_actualizacion=NOW() WHERE id_tarifa=?";
        try (Connection cn = ConexionDB.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, tarifa.getIdEspecialidad());
            ps.setString(2, tarifa.getNombreTarifa());
            ps.setBigDecimal(3, tarifa.getMonto());
            ps.setString(4, tarifa.getMoneda());
            ps.setString(5, tarifa.getVigenciaDesde());
            ps.setString(6, tarifa.getVigenciaHasta() == null || tarifa.getVigenciaHasta().isBlank() ? null : tarifa.getVigenciaHasta());
            ps.setString(7, tarifa.getEstadoRegistro());
            ps.setString(8, usuario);
            ps.setInt(9, tarifa.getIdTarifa());
            ps.executeUpdate();
        }
    }

    public void desactivar(int idTarifa, String usuario) throws SQLException {
        String sql = "UPDATE tarifa_consulta SET estado_registro='INACTIVO', usuario_actualizacion=?, fecha_actualizacion=NOW() WHERE id_tarifa=?";
        try (Connection cn = ConexionDB.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, usuario);
            ps.setInt(2, idTarifa);
            ps.executeUpdate();
        }
    }

    public java.math.BigDecimal obtenerTarifaVigentePorMedico(int idMedico, String fecha) throws SQLException {
        TarifaConsulta tarifa = obtenerTarifaVigenteDetallePorMedico(idMedico, fecha);
        return tarifa == null ? java.math.BigDecimal.ZERO : tarifa.getMonto();
    }

    /** Devuelve la tarifa vigente completa para congelar precio e id_tarifa en la cita. */
    public TarifaConsulta obtenerTarifaVigenteDetallePorMedico(int idMedico, String fecha) throws SQLException {
        String sql = "SELECT t.id_tarifa, t.id_especialidad, e.nombre AS especialidad, t.nombre_tarifa, t.monto, "
                + "t.moneda, t.vigencia_desde, t.vigencia_hasta, t.estado_registro "
                + "FROM medico m INNER JOIN especialidad e ON m.id_especialidad=e.id_especialidad "
                + "INNER JOIN tarifa_consulta t ON m.id_especialidad=t.id_especialidad "
                + "WHERE m.id_medico=? AND t.estado_registro='ACTIVO' AND t.vigencia_desde <= ? "
                + "AND (t.vigencia_hasta IS NULL OR t.vigencia_hasta >= ?) "
                + "ORDER BY t.vigencia_desde DESC LIMIT 1";
        try (Connection cn = ConexionDB.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idMedico);
            ps.setString(2, fecha);
            ps.setString(3, fecha);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapper(rs);
            }
        }
        return null;
    }

    /** Lista tarifas activas para mostrarlas a recepción y caja como guía de precios. */
    public List<TarifaConsulta> listarActivas() throws SQLException {
        List<TarifaConsulta> tarifas = new ArrayList<>();
        String sql = "SELECT t.id_tarifa, t.id_especialidad, e.nombre AS especialidad, t.nombre_tarifa, t.monto, "
                + "t.moneda, t.vigencia_desde, t.vigencia_hasta, t.estado_registro "
                + "FROM tarifa_consulta t INNER JOIN especialidad e ON t.id_especialidad = e.id_especialidad "
                + "WHERE t.estado_registro='ACTIVO' AND t.vigencia_desde <= CURDATE() "
                + "AND (t.vigencia_hasta IS NULL OR t.vigencia_hasta >= CURDATE()) "
                + "ORDER BY e.nombre ASC";
        try (Connection cn = ConexionDB.getConnection(); PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) tarifas.add(mapper(rs));
        }
        return tarifas;
    }

    private TarifaConsulta mapper(ResultSet rs) throws SQLException {
        TarifaConsulta tarifa = new TarifaConsulta();
        tarifa.setIdTarifa(rs.getInt("id_tarifa"));
        tarifa.setIdEspecialidad(rs.getInt("id_especialidad"));
        tarifa.setEspecialidad(rs.getString("especialidad"));
        tarifa.setNombreTarifa(rs.getString("nombre_tarifa"));
        tarifa.setMonto(rs.getBigDecimal("monto"));
        tarifa.setMoneda(rs.getString("moneda"));
        tarifa.setVigenciaDesde(rs.getString("vigencia_desde"));
        tarifa.setVigenciaHasta(rs.getString("vigencia_hasta"));
        tarifa.setEstadoRegistro(rs.getString("estado_registro"));
        return tarifa;
    }
}
