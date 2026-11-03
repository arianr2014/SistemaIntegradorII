package pe.edu.utp.farmacia.dao;

import pe.edu.utp.farmacia.model.Proveedor;
import pe.edu.utp.farmacia.util.ConexionDB;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDAOImpl implements ProveedorDAO {

    @Override
    public List<Proveedor> listar() throws SQLException {
        List<Proveedor> lista = new ArrayList<>();
        String sp = "{CALL sp_listar_proveedores()}";

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
    public Proveedor obtenerPorId(int id) throws SQLException {
        String sp = "{CALL sp_obtener_proveedor_por_id(?)}";
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
    public int registrar(Proveedor p, String usuarioRegistro) throws SQLException {
        String sp = "{CALL sp_registrar_proveedor(?, ?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sp)) {
            cs.setString(1, p.getRazonSocial());
            cs.setString(2, p.getRuc());
            cs.setString(3, p.getTelefono());
            cs.setString(4, p.getCorreo());
            cs.setString(5, p.getDireccion());
            cs.setString(6, usuarioRegistro);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id_proveedor");
                    p.setIdProveedor(id);
                    return id;
                }
            }
        }
        return 0;
    }

    @Override
    public void actualizar(Proveedor p, String usuarioActualizacion) throws SQLException {
        String sp = "{CALL sp_actualizar_proveedor(?, ?, ?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sp)) {
            cs.setInt(1, p.getIdProveedor());
            cs.setString(2, p.getRazonSocial());
            cs.setString(3, p.getRuc());
            cs.setString(4, p.getTelefono());
            cs.setString(5, p.getCorreo());
            cs.setString(6, p.getDireccion());
            cs.setString(7, usuarioActualizacion);
            cs.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id, String usuarioActualizacion) throws SQLException {
        String sp = "{CALL sp_eliminar_proveedor(?, ?)}";
        try (Connection conn = ConexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sp)) {
            cs.setInt(1, id);
            cs.setString(2, usuarioActualizacion);
            cs.executeUpdate();
        }
    }

    private Proveedor mapearResultSet(ResultSet rs) throws SQLException {
        Proveedor p = new Proveedor();
        p.setIdProveedor(rs.getInt("id_proveedor"));
        p.setRazonSocial(rs.getString("razon_social"));
        p.setRuc(rs.getString("ruc"));
        p.setTelefono(rs.getString("telefono"));
        p.setCorreo(rs.getString("correo"));
        p.setDireccion(rs.getString("direccion"));
        p.setUsuarioRegistro(rs.getString("usuario_registro"));
        p.setFechaRegistro(rs.getTimestamp("fecha_registro"));
        p.setEstadoRegistro(rs.getString("estado_registro"));
        return p;
    }
}
