package pe.edu.utp.farmacia.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.utp.farmacia.model.Prescripcion;
import pe.edu.utp.farmacia.model.PrescripcionDetalle;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PrescripcionDAOTest {

    private PrescripcionDAO dao;

    @BeforeEach
    public void setUp() {
        dao = new PrescripcionDAOImpl();
    }

    @Test
    @DisplayName("Prueba de Inserción de Prescripción y Sus Detalles")
    public void testRegistrarYObtenerPrescripcion() throws SQLException {
        long timestamp = System.currentTimeMillis();
        Prescripcion p = new Prescripcion();
        p.setCodigoPrescripcion("PRES-TST-" + (timestamp % 100000));
        p.setIdPacienteExterno(1);
        p.setIdMedicoExterno(2);
        p.setIdAtencionExterna(100);
        p.setFechaPrescripcion(new Timestamp(System.currentTimeMillis()));
        p.setInstruccionesGenerales("Instrucciones de prueba automatizada JUnit");
        p.setEstado("PENDIENTE");

        List<PrescripcionDetalle> detalles = new ArrayList<>();
        PrescripcionDetalle d1 = new PrescripcionDetalle();
        d1.setIdMedicamento(1);
        d1.setNombreMedicamento("Paracetamol 500 mg Tabletas");
        d1.setCantidad(10);
        d1.setDosis("1 tableta");
        d1.setFrecuencia("Cada 8 hrs");
        d1.setDuracion("3 días");
        d1.setIndicaciones("Para la fiebre");
        detalles.add(d1);

        p.setDetalles(detalles);

        int idGenerado = dao.registrar(p, "test_runner");
        assertTrue(idGenerado > 0, "El ID generado de la prescripción debe ser mayor a 0");

        Prescripcion obtenida = dao.obtenerPorId(idGenerado);
        assertNotNull(obtenida);
        assertEquals(p.getCodigoPrescripcion(), obtenida.getCodigoPrescripcion());
        assertNotNull(obtenida.getDetalles());
        assertFalse(obtenida.getDetalles().isEmpty());
    }

    @Test
    @DisplayName("Prueba de Listado de Prescripciones")
    public void testListarPrescripciones() throws SQLException {
        List<Prescripcion> lista = dao.listar();
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
    }
}
