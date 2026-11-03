package pe.edu.utp.farmacia.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.utp.farmacia.model.Medicamento;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MedicamentoDAOTest {

    private MedicamentoDAO dao;

    @BeforeEach
    public void setUp() {
        dao = new MedicamentoDAOImpl();
    }

    @Test
    @DisplayName("Prueba de Inserción y Consulta de Medicamentos")
    public void testRegistrarYObtenerMedicamento() throws SQLException {
        long timestamp = System.currentTimeMillis();
        Medicamento m = new Medicamento();
        m.setCodigo("TST-" + (timestamp % 1000000));
        m.setNombre("Medicamento Prueba " + timestamp);
        m.setPrincipioActivo("Principio Activo Test");
        m.setConcentracion("500 mg");
        m.setFormaFarmaceutica("Tableta");
        m.setPresentacion("Caja x 20");
        m.setIdCategoria(1); // Categoría preexistente de Analgésicos
        m.setPrecioVenta(new BigDecimal("2.50"));
        m.setRequiereReceta(false);

        int idGenerado = dao.registrar(m, "test_runner");
        assertTrue(idGenerado > 0, "El ID generado del medicamento debe ser mayor a 0");

        Medicamento obtenido = dao.obtenerPorId(idGenerado);
        assertNotNull(obtenido, "El medicamento registrado debe ser retornado por su ID");
        assertEquals(m.getNombre(), obtenido.getNombre());
    }

    @Test
    @DisplayName("Prueba de Listado de Medicamentos")
    public void testListarMedicamentos() throws SQLException {
        List<Medicamento> lista = dao.listar();
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
    }
}
