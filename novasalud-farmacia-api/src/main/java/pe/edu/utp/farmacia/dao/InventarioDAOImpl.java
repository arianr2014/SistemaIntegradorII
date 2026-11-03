package pe.edu.utp.farmacia.dao;

import pe.edu.utp.farmacia.model.Inventario;
import pe.edu.utp.farmacia.util.ConexionDB;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventarioDAOImpl implements InventarioDAO {

    @Override
    public List<Inventario> listar() throws SQLException {
        List<Inventario> lista = new ArrayList<>();
        String sp = "{CALL sp_listar_inventario()}";

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
    public Inventario obtenerPorId(int id) throws SQLException {
        String sp = "{CALL sp_obtener_inventario_por_id(?)}";
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
    public int registrar(Inventario inv, String usuarioRegistro) throws SQLException {
        String sp = "{CALL sp_registrar_inventario(?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sp)) {
            cs.setInt(1, inv.getIdMedicamento());
            cs.setInt(2, inv.getStockActual());
            cs.setInt(3, inv.getStockMinimo());
            cs.setString(4, inv.getUbicacion());
            cs.setString(5, usuarioRegistro);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id_inventario");
                    inv.setIdInventario(id);
                    return id;
                }
            }
        }
        return 0;
    }

    @Override
    public void actualizar(Inventario inv, String usuarioActualizacion) throws SQLException {
        String sp = "{CALL sp_actualizar_inventario(?, ?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sp)) {
            cs.setInt(1, inv.getIdInventario());
            cs.setInt(2, inv.getIdMedicamento());
            cs.setInt(3, inv.getStockActual());
            cs.setInt(4, inv.getStockMinimo());
            cs.setString(5, inv.getUbicacion());
            cs.setString(6, usuarioActualizacion);
            cs.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id, String usuarioActualizacion) throws SQLException {
        String sp = "{CALL sp_eliminar_inventario(?, ?)}";
        try (Connection conn = ConexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sp)) {
            cs.setInt(1, id);
            cs.setString(2, usuarioActualizacion);
            cs.executeUpdate();
        }
    }

    private Inventario mapearResultSet(ResultSet rs) throws SQLException {
        Inventario i = new Inventario();
        i.setIdInventario(rs.getInt("id_inventario"));
        i.setIdMedicamento(rs.getInt("id_medicamento"));
        i.setCodigoMedicamento(rs.getString("codigo_medicamento"));
        i.setNombreMedicamento(rs.getString("nombre_medicamento"));
        i.setStockActual(rs.getInt("stock_actual"));
        i.setStockMinimo(rs.getInt("stock_minimo"));
        i.setUbicacion(rs.getString("ubicacion"));
        i.setUsuarioRegistro(rs.getString("usuario_registro"));
        i.setFechaRegistro(rs.getTimestamp("fecha_registro"));
        i.setEstadoRegistro(rs.getString("estado_registro"));
        return i;
    }
}
