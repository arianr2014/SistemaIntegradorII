package pe.edu.utp.farmacia.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.utp.farmacia.model.CategoriaMedicamento;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CategoriaMedicamentoDAOTest {

    private CategoriaMedicamentoDAO dao;

    @BeforeEach
    public void setUp() {
        dao = new CategoriaMedicamentoDAOImpl();
    }

    @Test
    @DisplayName("Prueba de Inserción y Consulta de Categoría de Medicamentos")
    public void testRegistrarYObtenerCategoria() throws SQLException {
        CategoriaMedicamento c = new CategoriaMedicamento();
        c.setNombre("Categoría Test " + System.currentTimeMillis());
        c.setDescripcion("Descripción de prueba para automatización JUnit");

        int idGenerado = dao.registrar(c, "test_runner");
        assertTrue(idGenerado > 0, "El ID generado para la categoría debe ser mayor a 0");

        CategoriaMedicamento obtenida = dao.obtenerPorId(idGenerado);
        assertNotNull(obtenida, "La categoría registrada debe ser encontrada por su ID");
        assertEquals(c.getNombre(), obtenida.getNombre(), "El nombre de la categoría debe coincidir");
    }

    @Test
    @DisplayName("Prueba de Listado de Categorías de Medicamentos")
    public void testListarCategorias() throws SQLException {
        List<CategoriaMedicamento> lista = dao.listar();
        assertNotNull(lista, "La lista de categorías no debe ser nula");
        assertFalse(lista.isEmpty(), "La lista de categorías no debe estar vacía");
    }
}
