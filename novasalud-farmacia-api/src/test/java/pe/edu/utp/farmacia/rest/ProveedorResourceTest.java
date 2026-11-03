package pe.edu.utp.farmacia.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.utp.farmacia.model.Proveedor;
import pe.edu.utp.farmacia.service.ProveedorService;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProveedorResourceTest {

    private ProveedorService service;

    @BeforeEach
    public void setUp() {
        service = new ProveedorService();
    }

    @Test
    @DisplayName("Prueba de Servicio de Proveedores: Listar y Registrar")
    public void testListarYRegistrarProveedor() throws SQLException {
        List<Proveedor> lista = service.listar();
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        long ts = System.currentTimeMillis();
        Proveedor p = new Proveedor();
        p.setRazonSocial("Proveedor REST " + ts);
        p.setRuc("20" + (ts % 1000000000L));
        p.setTelefono("01-9998877");
        p.setCorreo("contacto@proveedorrest.com");
        p.setDireccion("Calle REST API 456");

        int id = service.registrar(p, "rest_test_runner");
        assertTrue(id > 0);
    }
}
