package pe.edu.utp.farmacia.dao;

import pe.edu.utp.farmacia.model.Venta;
import pe.edu.utp.farmacia.model.VentaDetalle;
import pe.edu.utp.farmacia.util.ConexionDB;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VentaDAOImpl implements VentaDAO {

    @Override
    public List<Venta> listar() throws SQLException {
        List<Venta> lista = new ArrayList<>();
        String sp = "{CALL sp_listar_ventas()}";

        try (Connection conn = ConexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sp);
             ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearResultSetVenta(rs));
            }
        }
        return lista;
    }

    @Override
    public Venta obtenerPorId(int id) throws SQLException {
        String sp = "{CALL sp_obtener_venta_por_id(?)}";
        Venta venta = null;

        try (Connection conn = ConexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sp)) {
            cs.setInt(1, id);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    venta = mapearResultSetVenta(rs);
                }
            }
        }

        if (venta != null) {
            venta.setDetalles(listarDetalles(id));
        }

        return venta;
    }

    @Override
    public int registrar(Venta v, String usuarioRegistro) throws SQLException {
        String sp = "{CALL sp_registrar_venta(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        int idGenerado = 0;

        try (Connection conn = ConexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sp)) {
            cs.setString(1, v.getCodigoVenta());
            cs.setString(2, v.getDni());
            cs.setString(3, v.getNombres());
            cs.setString(4, v.getApellidos());
            if (v.getIdPrescripcion() != null) cs.setInt(5, v.getIdPrescripcion()); else cs.setNull(5, java.sql.Types.INTEGER);
            cs.setTimestamp(6, v.getFechaVenta());
            cs.setBigDecimal(7, v.getSubtotal());
            cs.setBigDecimal(8, v.getIgv());
            cs.setBigDecimal(9, v.getTotal());
            cs.setString(10, v.getMetodoPago());
            if (v.getIdUsuario() != null) cs.setInt(11, v.getIdUsuario()); else cs.setNull(11, java.sql.Types.INTEGER);
            cs.setString(12, usuarioRegistro);

            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    idGenerado = rs.getInt("id_venta");
                    v.setIdVenta(idGenerado);
                }
            }
        }

        if (idGenerado > 0 && v.getDetalles() != null && !v.getDetalles().isEmpty()) {
            for (VentaDetalle detalle : v.getDetalles()) {
                detalle.setIdVenta(idGenerado);
                registrarDetalle(detalle, usuarioRegistro);
            }
        }

        return idGenerado;
    }

    @Override
    public void actualizar(Venta v, String usuarioActualizacion) throws SQLException {
        String sp = "{CALL sp_actualizar_venta(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sp)) {
            cs.setInt(1, v.getIdVenta());
            cs.setString(2, v.getCodigoVenta());
            cs.setString(3, v.getDni());
            cs.setString(4, v.getNombres());
            cs.setString(5, v.getApellidos());
            if (v.getIdPrescripcion() != null) cs.setInt(6, v.getIdPrescripcion()); else cs.setNull(6, java.sql.Types.INTEGER);
            cs.setTimestamp(7, v.getFechaVenta());
            cs.setBigDecimal(8, v.getSubtotal());
            cs.setBigDecimal(9, v.getIgv());
            cs.setBigDecimal(10, v.getTotal());
            cs.setString(11, v.getMetodoPago());
            if (v.getIdUsuario() != null) cs.setInt(12, v.getIdUsuario()); else cs.setNull(12, java.sql.Types.INTEGER);
            cs.setString(13, usuarioActualizacion);
            cs.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id, String usuarioActualizacion) throws SQLException {
        String sp = "{CALL sp_eliminar_venta(?, ?)}";
        try (Connection conn = ConexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sp)) {
            cs.setInt(1, id);
            cs.setString(2, usuarioActualizacion);
            cs.executeUpdate();
        }
    }

    @Override
    public List<VentaDetalle> listarDetalles(int idVenta) throws SQLException {
        List<VentaDetalle> detalles = new ArrayList<>();
        String sp = "{CALL sp_listar_venta_detalles_por_venta(?)}";

        try (Connection conn = ConexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sp)) {
            cs.setInt(1, idVenta);
            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    VentaDetalle d = new VentaDetalle();
                    d.setIdDetalle(rs.getInt("id_detalle"));
                    d.setIdVenta(rs.getInt("id_venta"));
                    d.setIdMedicamento(rs.getInt("id_medicamento"));
                    d.setNombreMedicamento(rs.getString("nombre_medicamento"));
                    d.setCantidad(rs.getInt("cantidad"));
                    d.setPrecioUnitario(rs.getBigDecimal("precio_unitario"));
                    d.setSubtotal(rs.getBigDecimal("subtotal"));
                    d.setUsuarioRegistro(rs.getString("usuario_registro"));
                    d.setFechaRegistro(rs.getTimestamp("fecha_registro"));
                    d.setEstadoRegistro(rs.getString("estado_registro"));
                    detalles.add(d);
                }
            }
        }
        return detalles;
    }

    @Override
    public int registrarDetalle(VentaDetalle d, String usuarioRegistro) throws SQLException {
        String sp = "{CALL sp_registrar_venta_detalle(?, ?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sp)) {
            cs.setInt(1, d.getIdVenta());
            cs.setInt(2, d.getIdMedicamento());
            cs.setInt(3, d.getCantidad());
            cs.setBigDecimal(4, d.getPrecioUnitario());
            cs.setBigDecimal(5, d.getSubtotal());
            cs.setString(6, usuarioRegistro);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id_detalle");
                    d.setIdDetalle(id);
                    return id;
                }
            }
        }
        return 0;
    }

    private Venta mapearResultSetVenta(ResultSet rs) throws SQLException {
        Venta v = new Venta();
        v.setIdVenta(rs.getInt("id_venta"));
        v.setCodigoVenta(rs.getString("codigo_venta"));
        v.setDni(rs.getString("dni"));
        v.setNombres(rs.getString("nombres"));
        v.setApellidos(rs.getString("apellidos"));
        v.setIdPrescripcion(rs.getObject("id_prescripcion") != null ? rs.getInt("id_prescripcion") : null);
        v.setFechaVenta(rs.getTimestamp("fecha_venta"));
        v.setSubtotal(rs.getBigDecimal("subtotal"));
        v.setIgv(rs.getBigDecimal("igv"));
        v.setTotal(rs.getBigDecimal("total"));
        v.setMetodoPago(rs.getString("metodo_pago"));
        v.setIdUsuario(rs.getObject("id_usuario") != null ? rs.getInt("id_usuario") : null);
        v.setUsernameUsuario(rs.getString("username_usuario"));
        v.setUsuarioRegistro(rs.getString("usuario_registro"));
        v.setFechaRegistro(rs.getTimestamp("fecha_registro"));
        v.setEstadoRegistro(rs.getString("estado_registro"));
        return v;
    }
}
