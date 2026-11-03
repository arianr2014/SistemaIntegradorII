package pe.edu.utp.novasalud.backend.dao;

import pe.edu.utp.novasalud.backend.model.Pago;
import pe.edu.utp.novasalud.backend.model.ReporteFinanciero;
import pe.edu.utp.novasalud.backend.util.ConexionDB;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** DAO de pagos, caja, comprobantes y reportes financieros básicos. */
public class PagoDAO {

    public void crearPagoPendienteSiNoExiste(int idCita, BigDecimal monto, String usuario) throws SQLException {
        String existe = "SELECT COUNT(*) FROM pago WHERE id_cita=? AND estado_registro='ACTIVO'";
        try (Connection cn = ConexionDB.getConnection(); PreparedStatement ps = cn.prepareStatement(existe)) {
            ps.setInt(1, idCita);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next() && rs.getInt(1) > 0) return; }
        }
        String sql = "INSERT INTO pago(id_cita, monto_total, monto_pagado, estado_pago, usuario_registro, fecha_registro, estado_registro) VALUES (?, ?, 0, 'PENDIENTE', ?, NOW(), 'ACTIVO')";
        try (Connection cn = ConexionDB.getConnection(); PreparedStatement ps = cn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, idCita);
            ps.setBigDecimal(2, monto == null ? BigDecimal.ZERO : monto);
            ps.setString(3, usuario);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    asignarCodigoPago(cn, keys.getInt(1));
                }
            }
        }
    }

    public List<Pago> listar(String fechaInicio, String fechaFin, String estadoPago) throws SQLException { return listar(fechaInicio, fechaFin, estadoPago, ""); }

    public List<Pago> listar(String fechaInicio, String fechaFin, String estadoPago, String dniPaciente) throws SQLException {
        List<Pago> pagos = new ArrayList<>();
        String sql = consultaBase()
                + "WHERE p.estado_registro='ACTIVO' "
                + "AND (?='' OR c.fecha >= ?) AND (?='' OR c.fecha <= ?) "
                + "AND (?='' OR p.estado_pago = ?) "
                + "AND (?='' OR pa.dni = ?) "
                + "ORDER BY c.fecha DESC, c.hora DESC";
        try (Connection cn = ConexionDB.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            setFiltroFechas(ps, fechaInicio, fechaFin, estadoPago);
            String dni = dniPaciente == null ? "" : dniPaciente.trim();
            ps.setString(7, dni); ps.setString(8, dni);
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) pagos.add(mapper(rs)); }
        }
        return pagos;
    }

    public Pago obtenerPorId(int idPago) throws SQLException {
        String sql = consultaBase() + "WHERE p.id_pago=?";
        try (Connection cn = ConexionDB.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idPago);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return mapper(rs); }
        }
        return null;
    }

    public void registrarPago(int idPago, BigDecimal monto, String metodoPago, String numeroOperacion, String observacion, int idUsuario) throws SQLException {
        try (Connection cn = ConexionDB.getConnection()) {
            cn.setAutoCommit(false);
            try {
                String detalle = "INSERT INTO pago_detalle(id_pago, metodo_pago, monto, numero_operacion, id_usuario_cajero, fecha_operacion, observacion) VALUES (?, ?, ?, ?, ?, NOW(), ?)";
                try (PreparedStatement ps = cn.prepareStatement(detalle)) {
                    ps.setInt(1, idPago);
                    ps.setString(2, metodoPago);
                    ps.setBigDecimal(3, monto);
                    ps.setString(4, numeroOperacion);
                    if (idUsuario > 0) ps.setInt(5, idUsuario); else ps.setNull(5, java.sql.Types.INTEGER);
                    ps.setString(6, observacion);
                    ps.executeUpdate();
                }

                String pago = "UPDATE pago SET monto_pagado = monto_pagado + ?, "
                        + "estado_pago = CASE WHEN monto_pagado + ? >= monto_total THEN 'PAGADO' ELSE 'PARCIAL' END, "
                        + "fecha_pago=NOW(), id_usuario_cobro=?, observacion=?, usuario_actualizacion=?, fecha_actualizacion=NOW() "
                        + "WHERE id_pago=? AND estado_pago NOT IN ('PAGADO','ANULADO')";
                try (PreparedStatement ps = cn.prepareStatement(pago)) {
                    ps.setBigDecimal(1, monto);
                    ps.setBigDecimal(2, monto);
                    if (idUsuario > 0) ps.setInt(3, idUsuario); else ps.setNull(3, java.sql.Types.INTEGER);
                    ps.setString(4, observacion);
                    ps.setString(5, String.valueOf(idUsuario));
                    ps.setInt(6, idPago);
                    ps.executeUpdate();
                }
                cn.commit();
            } catch (SQLException ex) {
                cn.rollback();
                throw ex;
            } finally {
                cn.setAutoCommit(true);
            }
        }
    }



    /**
     * Actualiza el monto de un pago pendiente solo si aún no tiene abonos registrados.
     * Se usa cuando recepción cambia médico/especialidad antes de que caja cobre.
     */
    public void actualizarMontoPendienteSinAbonos(int idCita, BigDecimal monto) throws SQLException {
        String sql = "UPDATE pago p "
                + "LEFT JOIN pago_detalle d ON p.id_pago=d.id_pago AND d.estado_registro='ACTIVO' "
                + "SET p.monto_total=?, p.monto_pagado=0, p.estado_pago='PENDIENTE', p.fecha_actualizacion=NOW() "
                + "WHERE p.id_cita=? AND p.estado_registro='ACTIVO' AND p.estado_pago='PENDIENTE' AND d.id_pago_detalle IS NULL";
        try (Connection cn = ConexionDB.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setBigDecimal(1, monto == null ? BigDecimal.ZERO : monto);
            ps.setInt(2, idCita);
            ps.executeUpdate();
        }
    }

    public void anularPago(int idPago, String observacion) throws SQLException {
        String sql = "UPDATE pago SET estado_pago='ANULADO', observacion=?, fecha_actualizacion=NOW() WHERE id_pago=?";
        try (Connection cn = ConexionDB.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, observacion);
            ps.setInt(2, idPago);
            ps.executeUpdate();
        }
    }

    public String obtenerEstadoPagoPorCita(int idCita) throws SQLException {
        String sql = "SELECT estado_pago FROM pago WHERE id_cita=? AND estado_registro='ACTIVO' LIMIT 1";
        try (Connection cn = ConexionDB.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idCita);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return rs.getString("estado_pago"); }
        }
        return "PENDIENTE";
    }

    public List<ReporteFinanciero> ingresosPorEspecialidad(String fechaInicio, String fechaFin) throws SQLException {
        String sql = "SELECT e.nombre AS etiqueta, COUNT(*) AS cantidad, COALESCE(SUM(p.monto_pagado),0) AS total "
                + "FROM pago p INNER JOIN cita c ON p.id_cita=c.id_cita INNER JOIN medico m ON c.id_medico=m.id_medico INNER JOIN especialidad e ON m.id_especialidad=e.id_especialidad "
                + "WHERE p.estado_registro='ACTIVO' AND p.estado_pago IN ('PAGADO','PARCIAL') AND c.fecha BETWEEN ? AND ? GROUP BY e.nombre ORDER BY total DESC";
        return reporte(sql, fechaInicio, fechaFin);
    }

    public List<ReporteFinanciero> ingresosPorMetodoPago(String fechaInicio, String fechaFin) throws SQLException {
        String sql = "SELECT d.metodo_pago AS etiqueta, COUNT(*) AS cantidad, COALESCE(SUM(d.monto),0) AS total "
                + "FROM pago_detalle d INNER JOIN pago p ON d.id_pago=p.id_pago INNER JOIN cita c ON p.id_cita=c.id_cita "
                + "WHERE p.estado_registro='ACTIVO' AND c.fecha BETWEEN ? AND ? GROUP BY d.metodo_pago ORDER BY total DESC";
        return reporte(sql, fechaInicio, fechaFin);
    }

    public BigDecimal totalIngresos(String fechaInicio, String fechaFin) throws SQLException {
        String sql = "SELECT COALESCE(SUM(d.monto),0) AS total FROM pago_detalle d INNER JOIN pago p ON d.id_pago=p.id_pago INNER JOIN cita c ON p.id_cita=c.id_cita WHERE p.estado_registro='ACTIVO' AND c.fecha BETWEEN ? AND ?";
        try (Connection cn = ConexionDB.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, fechaInicio);
            ps.setString(2, fechaFin);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return rs.getBigDecimal("total"); }
        }
        return BigDecimal.ZERO;
    }

    private List<ReporteFinanciero> reporte(String sql, String inicio, String fin) throws SQLException {
        List<ReporteFinanciero> lista = new ArrayList<>();
        try (Connection cn = ConexionDB.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, inicio);
            ps.setString(2, fin);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ReporteFinanciero r = new ReporteFinanciero();
                    r.setEtiqueta(rs.getString("etiqueta"));
                    r.setCantidad(rs.getInt("cantidad"));
                    r.setTotal(rs.getBigDecimal("total"));
                    lista.add(r);
                }
            }
        }
        return lista;
    }



    /** Genera el código interno legible del pago a partir del ID autoincremental. */
    private void asignarCodigoPago(Connection cn, int idPago) throws SQLException {
        String sql = "UPDATE pago SET codigo_pago = CONCAT('PAG-', LPAD(?, 6, '0')) WHERE id_pago = ? AND codigo_pago IS NULL";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idPago);
            ps.setInt(2, idPago);
            ps.executeUpdate();
        }
    }

    private String consultaBase() {
        return "SELECT p.id_pago, p.id_cita, p.codigo_pago, pa.dni AS paciente_dni, CONCAT(pa.nombres,' ',pa.apellidos) AS paciente, "
                + "CONCAT(m.nombres,' ',m.apellidos) AS medico, e.nombre AS especialidad, c.fecha AS fecha_cita, c.hora AS hora_cita, "
                + "p.monto_subtotal, p.monto_igv, p.monto_total, p.monto_pagado, p.estado_pago, p.fecha_pago, p.observacion, COALESCE(u.username,'') AS usuario_cobro, "
                + "COALESCE((SELECT GROUP_CONCAT(CONCAT(d.metodo_pago, ' S/ ', FORMAT(d.monto, 2)) ORDER BY d.id_pago_detalle SEPARATOR ', ') FROM pago_detalle d WHERE d.id_pago=p.id_pago AND d.estado_registro='ACTIVO'), '') AS metodos_pago, "
                + "COALESCE((SELECT GROUP_CONCAT(NULLIF(d.numero_operacion,'') ORDER BY d.id_pago_detalle SEPARATOR ', ') FROM pago_detalle d WHERE d.id_pago=p.id_pago AND d.estado_registro='ACTIVO'), '') AS operaciones_externas "
                + "FROM pago p INNER JOIN cita c ON p.id_cita=c.id_cita INNER JOIN paciente pa ON c.id_paciente=pa.id_paciente "
                + "INNER JOIN medico m ON c.id_medico=m.id_medico INNER JOIN especialidad e ON m.id_especialidad=e.id_especialidad "
                + "LEFT JOIN usuario u ON p.id_usuario_cobro=u.id_usuario ";
    }

    private void setFiltroFechas(PreparedStatement ps, String inicio, String fin, String estado) throws SQLException {
        String i = inicio == null ? "" : inicio.trim();
        String f = fin == null ? "" : fin.trim();
        String e = estado == null ? "" : estado.trim().toUpperCase();
        ps.setString(1, i); ps.setString(2, i);
        ps.setString(3, f); ps.setString(4, f);
        ps.setString(5, e); ps.setString(6, e);
    }

    private Pago mapper(ResultSet rs) throws SQLException {
        Pago pago = new Pago();
        pago.setIdPago(rs.getInt("id_pago"));
        pago.setIdCita(rs.getInt("id_cita"));
        pago.setCodigoPago(rs.getString("codigo_pago"));
        pago.setPacienteDni(rs.getString("paciente_dni"));
        pago.setPaciente(rs.getString("paciente"));
        pago.setMedico(rs.getString("medico"));
        pago.setEspecialidad(rs.getString("especialidad"));
        pago.setFechaCita(rs.getString("fecha_cita"));
        pago.setHoraCita(String.valueOf(rs.getTime("hora_cita")));
        pago.setMontoSubtotal(rs.getBigDecimal("monto_subtotal"));
        pago.setMontoIgv(rs.getBigDecimal("monto_igv"));
        pago.setMontoTotal(rs.getBigDecimal("monto_total"));
        pago.setMontoPagado(rs.getBigDecimal("monto_pagado"));
        pago.setEstadoPago(rs.getString("estado_pago"));
        pago.setFechaPago(rs.getString("fecha_pago"));
        pago.setUsuarioCobro(rs.getString("usuario_cobro"));
        pago.setObservacion(rs.getString("observacion"));
        pago.setMetodosPago(rs.getString("metodos_pago"));
        pago.setOperacionesExternas(rs.getString("operaciones_externas"));
        return pago;
    }
}
