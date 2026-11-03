package pe.edu.utp.farmacia.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.utp.farmacia.model.Medicamento;
import pe.edu.utp.farmacia.service.MedicamentoService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MedicamentoResourceTest {

    private MedicamentoService service;

    @BeforeEach
    public void setUp() {
        service = new MedicamentoService();
    }

    @Test
    @DisplayName("Prueba de Servicio de Medicamentos: Listar y Registrar")
    public void testListarYRegistrarMedicamento() throws SQLException {
        List<Medicamento> lista = service.listar();
        assertNotNull(lista);
        assertFalse(lista.isEmpty(), "La lista de medicamentos no debe estar vacía");

        long ts = System.currentTimeMillis();
        Medicamento m = new Medicamento();
        m.setCodigo("MED-RST-" + (ts % 100000));
        m.setNombre("Medicamento REST Test " + ts);
        m.setPrincipioActivo("Principio REST");
        m.setConcentracion("100 mg");
        m.setFormaFarmaceutica("Cápsula");
        m.setPresentacion("Caja x 10");
        m.setIdCategoria(1);
        m.setPrecioVenta(new BigDecimal("3.50"));
        m.setRequiereReceta(false);

        int id = service.registrar(m, "rest_test_runner");
        assertTrue(id > 0);
    }
}
