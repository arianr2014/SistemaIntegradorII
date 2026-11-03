package pe.edu.utp.farmacia.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.utp.farmacia.model.Proveedor;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProveedorDAOTest {

    private ProveedorDAO dao;

    @BeforeEach
    public void setUp() {
        dao = new ProveedorDAOImpl();
    }

    @Test
    @DisplayName("Prueba de Inserción y Consulta de Proveedores")
    public void testRegistrarYObtenerProveedor() throws SQLException {
        Proveedor p = new Proveedor();
        p.setRazonSocial("Proveedor Test S.A.C. " + System.currentTimeMillis());
        p.setRuc("2099988877" + (System.currentTimeMillis() % 10));
        p.setTelefono("01-5554321");
        p.setCorreo("test@proveedortest.com");
        p.setDireccion("Av. Pruebas Automatizadas 123");

        int idGenerado = dao.registrar(p, "test_runner");
        assertTrue(idGenerado > 0, "El ID generado del proveedor debe ser mayor a 0");

        Proveedor obtenido = dao.obtenerPorId(idGenerado);
        assertNotNull(obtenido, "El proveedor registrado debe poder consultarse por ID");
        assertEquals(p.getRazonSocial(), obtenido.getRazonSocial());
    }

    @Test
    @DisplayName("Prueba de Listado de Proveedores")
    public void testListarProveedores() throws SQLException {
        List<Proveedor> lista = dao.listar();
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
    }
}
