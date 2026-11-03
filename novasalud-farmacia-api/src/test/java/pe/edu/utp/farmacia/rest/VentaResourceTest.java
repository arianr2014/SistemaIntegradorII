package pe.edu.utp.farmacia.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.utp.farmacia.model.Venta;
import pe.edu.utp.farmacia.model.VentaDetalle;
import pe.edu.utp.farmacia.service.VentaService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VentaResourceTest {

    private VentaService service;

    @BeforeEach
    public void setUp() {
        service = new VentaService();
    }

    @Test
    @DisplayName("Prueba de Servicio de Ventas: Listar y Registrar Venta con Detalles")
    public void testListarYRegistrarVenta() throws SQLException {
        List<Venta> lista = service.listar();
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        long ts = System.currentTimeMillis();
        Venta v = new Venta();
        v.setCodigoVenta("VEN-RST-" + (ts % 100000));
        v.setDni("88776655");
        v.setNombres("Cliente REST");
        v.setApellidos("JUnit REST Test");
        v.setFechaVenta(new Timestamp(System.currentTimeMillis()));
        v.setSubtotal(new BigDecimal("12.71"));
        v.setIgv(new BigDecimal("2.29"));
        v.setTotal(new BigDecimal("15.00"));
        v.setMetodoPago("TARJETA_CREDITO");
        v.setIdUsuario(1);

        List<VentaDetalle> detalles = new ArrayList<>();
        VentaDetalle d1 = new VentaDetalle();
        d1.setIdMedicamento(1);
        d1.setCantidad(5);
        d1.setPrecioUnitario(new BigDecimal("3.00"));
        d1.setSubtotal(new BigDecimal("15.00"));
        detalles.add(d1);

        v.setDetalles(detalles);

        int id = service.registrar(v, "rest_test_runner");
        assertTrue(id > 0);
    }
}
