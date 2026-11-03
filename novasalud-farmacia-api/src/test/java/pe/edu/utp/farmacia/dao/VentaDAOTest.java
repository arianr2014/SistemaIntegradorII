package pe.edu.utp.farmacia.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.utp.farmacia.model.Venta;
import pe.edu.utp.farmacia.model.VentaDetalle;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VentaDAOTest {

    private VentaDAO dao;

    @BeforeEach
    public void setUp() {
        dao = new VentaDAOImpl();
    }

    @Test
    @DisplayName("Prueba de Inserción de Venta y sus Detalles de Venta")
    public void testRegistrarYObtenerVenta() throws SQLException {
        long timestamp = System.currentTimeMillis();
        Venta v = new Venta();
        v.setCodigoVenta("VEN-TST-" + (timestamp % 100000));
        v.setDni("77889900");
        v.setNombres("Cliente");
        v.setApellidos("Prueba JUnit");
        v.setFechaVenta(new Timestamp(System.currentTimeMillis()));
        v.setSubtotal(new BigDecimal("16.95"));
        v.setIgv(new BigDecimal("3.05"));
        v.setTotal(new BigDecimal("20.00"));
        v.setMetodoPago("EFECTIVO");
        v.setIdUsuario(1);

        List<VentaDetalle> detalles = new ArrayList<>();
        VentaDetalle d1 = new VentaDetalle();
        d1.setIdMedicamento(1);
        d1.setCantidad(10);
        d1.setPrecioUnitario(new BigDecimal("1.00"));
        d1.setSubtotal(new BigDecimal("10.00"));
        detalles.add(d1);

        VentaDetalle d2 = new VentaDetalle();
        d2.setIdMedicamento(5);
        d2.setCantidad(10);
        d2.setPrecioUnitario(new BigDecimal("1.00"));
        d2.setSubtotal(new BigDecimal("10.00"));
        detalles.add(d2);

        v.setDetalles(detalles);

        int idGenerado = dao.registrar(v, "test_runner");
        assertTrue(idGenerado > 0, "El ID generado para la venta debe ser mayor a 0");

        Venta obtenida = dao.obtenerPorId(idGenerado);
        assertNotNull(obtenida);
        assertEquals(v.getCodigoVenta(), obtenida.getCodigoVenta());
        assertNotNull(obtenida.getDetalles());
        assertEquals(2, obtenida.getDetalles().size(), "La venta registrada debe incluir exactamente 2 detalles");
    }

    @Test
    @DisplayName("Prueba de Listado de Ventas")
    public void testListarVentas() throws SQLException {
        List<Venta> lista = dao.listar();
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
    }
}
