package pe.edu.utp.farmacia.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.utp.farmacia.model.CategoriaMedicamento;
import pe.edu.utp.farmacia.service.CategoriaMedicamentoService;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CategoriaResourceTest {

    private CategoriaMedicamentoService service;

    @BeforeEach
    public void setUp() {
        service = new CategoriaMedicamentoService();
    }

    @Test
    @DisplayName("Prueba de Servicio de Categorías: Listar y Obtener")
    public void testListarYObtenerCategorias() throws SQLException {
        List<CategoriaMedicamento> lista = service.listar();
        assertNotNull(lista);
        assertFalse(lista.isEmpty(), "La lista de categorías debe contener elementos");

        CategoriaMedicamento primera = lista.get(0);
        CategoriaMedicamento obtenida = service.obtenerPorId(primera.getIdCategoria());
        assertNotNull(obtenida);
        assertEquals(primera.getNombre(), obtenida.getNombre());
    }

    @Test
    @DisplayName("Prueba de Registro de Categoría vía Servicio de Negocio")
    public void testRegistrarCategoria() throws SQLException {
        CategoriaMedicamento c = new CategoriaMedicamento();
        c.setNombre("Categoría REST " + System.currentTimeMillis());
        c.setDescripcion("Descripción de prueba REST API");

        int id = service.registrar(c, "rest_test_runner");
        assertTrue(id > 0);
    }
}
