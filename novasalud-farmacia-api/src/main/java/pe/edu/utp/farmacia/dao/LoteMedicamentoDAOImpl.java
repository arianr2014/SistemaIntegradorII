package pe.edu.utp.farmacia.dao;

import pe.edu.utp.farmacia.model.LoteMedicamento;
import pe.edu.utp.farmacia.util.ConexionDB;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoteMedicamentoDAOImpl implements LoteMedicamentoDAO {

    @Override
    public List<LoteMedicamento> listar() throws SQLException {
        List<LoteMedicamento> lista = new ArrayList<>();
        String sp = "{CALL sp_listar_lotes_medicamento()}";

        try (Connection conn = ConexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sp);
             ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearResultSet(rs));
            }
        }
        return lista;
    }

    @Override
    public LoteMedicamento obtenerPorId(int id) throws SQLException {
        String sp = "{CALL sp_obtener_lote_medicamento_por_id(?)}";
        try (Connection conn = ConexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sp)) {
            cs.setInt(1, id);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSet(rs);
                }
            }
        }
        return null;
    }

    @Override
    public int registrar(LoteMedicamento l, String usuarioRegistro) throws SQLException {
        String sp = "{CALL sp_registrar_lote_medicamento(?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sp)) {
            cs.setInt(1, l.getIdMedicamento());
            cs.setInt(2, l.getIdProveedor());
            cs.setString(3, l.getNumeroLote());
            cs.setDate(4, l.getFechaFabricacion());
            cs.setDate(5, l.getFechaVencimiento());
            cs.setInt(6, l.getCantidadInicial());
            cs.setInt(7, l.getCantidadDisponible());
            cs.setString(8, usuarioRegistro);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id_lote");
                    l.setIdLote(id);
                    return id;
                }
            }
        }
        return 0;
    }

    @Override
    public void actualizar(LoteMedicamento l, String usuarioActualizacion) throws SQLException {
        String sp = "{CALL sp_actualizar_lote_medicamento(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sp)) {
            cs.setInt(1, l.getIdLote());
            cs.setInt(2, l.getIdMedicamento());
            cs.setInt(3, l.getIdProveedor());
            cs.setString(4, l.getNumeroLote());
            cs.setDate(5, l.getFechaFabricacion());
            cs.setDate(6, l.getFechaVencimiento());
            cs.setInt(7, l.getCantidadInicial());
            cs.setInt(8, l.getCantidadDisponible());
            cs.setString(9, usuarioActualizacion);
            cs.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id, String usuarioActualizacion) throws SQLException {
        String sp = "{CALL sp_eliminar_lote_medicamento(?, ?)}";
        try (Connection conn = ConexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sp)) {
            cs.setInt(1, id);
            cs.setString(2, usuarioActualizacion);
            cs.executeUpdate();
        }
    }

    private LoteMedicamento mapearResultSet(ResultSet rs) throws SQLException {
        LoteMedicamento l = new LoteMedicamento();
        l.setIdLote(rs.getInt("id_lote"));
        l.setIdMedicamento(rs.getInt("id_medicamento"));
        l.setNombreMedicamento(rs.getString("nombre_medicamento"));
        l.setIdProveedor(rs.getInt("id_proveedor"));
        l.setRazonSocialProveedor(rs.getString("razon_social_proveedor"));
        l.setNumeroLote(rs.getString("numero_lote"));
        l.setFechaFabricacion(rs.getDate("fecha_fabricacion"));
        l.setFechaVencimiento(rs.getDate("fecha_vencimiento"));
        l.setCantidadInicial(rs.getInt("cantidad_inicial"));
        l.setCantidadDisponible(rs.getInt("cantidad_disponible"));
        l.setUsuarioRegistro(rs.getString("usuario_registro"));
        l.setFechaRegistro(rs.getTimestamp("fecha_registro"));
        l.setEstadoRegistro(rs.getString("estado_registro"));
        return l;
    }
}
