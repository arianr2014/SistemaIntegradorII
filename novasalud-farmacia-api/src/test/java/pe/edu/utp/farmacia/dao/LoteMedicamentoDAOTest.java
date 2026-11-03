package pe.edu.utp.farmacia.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.utp.farmacia.model.LoteMedicamento;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LoteMedicamentoDAOTest {

    private LoteMedicamentoDAO dao;

    @BeforeEach
    public void setUp() {
        dao = new LoteMedicamentoDAOImpl();
    }

    @Test
    @DisplayName("Prueba de Inserción y Consulta de Lote de Medicamentos")
    public void testRegistrarYObtenerLote() throws SQLException {
        long timestamp = System.currentTimeMillis();
        LoteMedicamento l = new LoteMedicamento();
        l.setIdMedicamento(1);
        l.setIdProveedor(1);
        l.setNumeroLote("LOT-TST-" + (timestamp % 100000));
        l.setFechaFabricacion(Date.valueOf("2026-01-01"));
        l.setFechaVencimiento(Date.valueOf("2028-12-31"));
        l.setCantidadInicial(500);
        l.setCantidadDisponible(500);

        int idGenerado = dao.registrar(l, "test_runner");
        assertTrue(idGenerado > 0, "El ID generado del lote debe ser mayor a 0");

        LoteMedicamento obtenido = dao.obtenerPorId(idGenerado);
        assertNotNull(obtenido);
        assertEquals(l.getNumeroLote(), obtenido.getNumeroLote());
    }

    @Test
    @DisplayName("Prueba de Listado de Lotes de Medicamentos")
    public void testListarLotes() throws SQLException {
        List<LoteMedicamento> lista = dao.listar();
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
    }
}
