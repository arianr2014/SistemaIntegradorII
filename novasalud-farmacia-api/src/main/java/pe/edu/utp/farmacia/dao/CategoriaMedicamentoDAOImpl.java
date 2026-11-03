package pe.edu.utp.farmacia.dao;

import pe.edu.utp.farmacia.model.CategoriaMedicamento;
import pe.edu.utp.farmacia.util.ConexionDB;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaMedicamentoDAOImpl implements CategoriaMedicamentoDAO {

    @Override
    public List<CategoriaMedicamento> listar() throws SQLException {
        List<CategoriaMedicamento> lista = new ArrayList<>();
        String sp = "{CALL sp_listar_categoria_medicamentos()}";

        try (Connection conn = ConexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sp);
             ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                CategoriaMedicamento c = new CategoriaMedicamento();
                c.setIdCategoria(rs.getInt("id_categoria"));
                c.setNombre(rs.getString("nombre"));
                c.setDescripcion(rs.getString("descripcion"));
                c.setUsuarioRegistro(rs.getString("usuario_registro"));
                c.setFechaRegistro(rs.getTimestamp("fecha_registro"));
                c.setEstadoRegistro(rs.getString("estado_registro"));
                lista.add(c);
            }
        }
        return lista;
    }

    @Override
    public CategoriaMedicamento obtenerPorId(int id) throws SQLException {
        String sp = "{CALL sp_obtener_categoria_medicamento_por_id(?)}";
        try (Connection conn = ConexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sp)) {
            cs.setInt(1, id);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    CategoriaMedicamento c = new CategoriaMedicamento();
                    c.setIdCategoria(rs.getInt("id_categoria"));
                    c.setNombre(rs.getString("nombre"));
                    c.setDescripcion(rs.getString("descripcion"));
                    c.setUsuarioRegistro(rs.getString("usuario_registro"));
                    c.setFechaRegistro(rs.getTimestamp("fecha_registro"));
                    c.setEstadoRegistro(rs.getString("estado_registro"));
                    return c;
                }
            }
        }
        return null;
    }

    @Override
    public int registrar(CategoriaMedicamento c, String usuarioRegistro) throws SQLException {
        String sp = "{CALL sp_registrar_categoria_medicamento(?, ?, ?)}";
        try (Connection conn = ConexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sp)) {
            cs.setString(1, c.getNombre());
            cs.setString(2, c.getDescripcion());
            cs.setString(3, usuarioRegistro);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    int idGenerado = rs.getInt("id_categoria");
                    c.setIdCategoria(idGenerado);
                    return idGenerado;
                }
            }
        }
        return 0;
    }

    @Override
    public void actualizar(CategoriaMedicamento c, String usuarioActualizacion) throws SQLException {
        String sp = "{CALL sp_actualizar_categoria_medicamento(?, ?, ?, ?)}";
        try (Connection conn = ConexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sp)) {
            cs.setInt(1, c.getIdCategoria());
            cs.setString(2, c.getNombre());
            cs.setString(3, c.getDescripcion());
            cs.setString(4, usuarioActualizacion);
            cs.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id, String usuarioActualizacion) throws SQLException {
        String sp = "{CALL sp_eliminar_categoria_medicamento(?, ?)}";
        try (Connection conn = ConexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sp)) {
            cs.setInt(1, id);
            cs.setString(2, usuarioActualizacion);
            cs.executeUpdate();
        }
    }
}
