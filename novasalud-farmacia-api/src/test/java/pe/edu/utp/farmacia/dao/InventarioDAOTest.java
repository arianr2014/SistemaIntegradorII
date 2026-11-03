package pe.edu.utp.farmacia.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.utp.farmacia.model.Inventario;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InventarioDAOTest {

    private InventarioDAO dao;

    @BeforeEach
    public void setUp() {
        dao = new InventarioDAOImpl();
    }

    @Test
    @DisplayName("Prueba de Consulta e Inserción de Inventario")
    public void testListarYObtenerInventario() throws SQLException {
        List<Inventario> lista = dao.listar();
        assertNotNull(lista);
        assertFalse(lista.isEmpty(), "El inventario debe contener elementos previamente poblados");

        Inventario primero = lista.get(0);
        Inventario obtenido = dao.obtenerPorId(primero.getIdInventario());
        assertNotNull(obtenido);
        assertEquals(primero.getIdMedicamento(), obtenido.getIdMedicamento());
    }
}
