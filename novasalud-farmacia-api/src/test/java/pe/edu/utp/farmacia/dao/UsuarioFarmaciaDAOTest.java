package pe.edu.utp.farmacia.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.utp.farmacia.model.UsuarioFarmacia;
import pe.edu.utp.farmacia.util.PasswordUtil;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioFarmaciaDAOTest {

    private UsuarioFarmaciaDAO dao;

    @BeforeEach
    public void setUp() {
        dao = new UsuarioFarmaciaDAOImpl();
    }

    @Test
    @DisplayName("Prueba de Inserción de Usuario con Password Encriptado en BCrypt")
    public void testRegistrarYObtenerUsuario() throws SQLException {
        long timestamp = System.currentTimeMillis();
        UsuarioFarmacia u = new UsuarioFarmacia();
        u.setUsername("usr_" + (timestamp % 100000));
        u.setNombres("Usuario Test");
        u.setApellidos("JUnit Runner");
        u.setRol("TECNICO_FARMACIA");
        u.setPasswordHash(PasswordUtil.hashPassword("Farmacia2026*"));

        int idGenerado = dao.registrar(u, "test_runner");
        assertTrue(idGenerado > 0, "El ID del usuario registrado debe ser mayor a 0");

        UsuarioFarmacia obtenido = dao.obtenerPorUsername(u.getUsername());
        assertNotNull(obtenido, "El usuario debe ser encontrado por username");
        assertTrue(PasswordUtil.checkPassword("Farmacia2026*", obtenido.getPasswordHash()), "La clave encriptada BCrypt debe validarse correctamente");
    }

    @Test
    @DisplayName("Prueba de Hash del Script SQL")
    public void testScriptHash() {
        String validHash = "$2a$12$ACgLkSwZBvZztr9EI3sZsOggQdTNAxBmRDjVdtoXk7KxCZoZymz9G";
        assertTrue(PasswordUtil.checkPassword("Farmacia2026*", validHash), "El hash BCrypt generado debe ser válido para Farmacia2026*");
    }

    @Test
    @DisplayName("Prueba de Listado de Usuarios de Farmacia")
    public void testListarUsuarios() throws SQLException {
        List<UsuarioFarmacia> lista = dao.listar();
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
    }
}
