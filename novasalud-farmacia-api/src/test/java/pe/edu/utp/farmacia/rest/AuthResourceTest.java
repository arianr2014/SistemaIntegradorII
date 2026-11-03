package pe.edu.utp.farmacia.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.utp.farmacia.model.UsuarioFarmacia;
import pe.edu.utp.farmacia.service.UsuarioFarmaciaService;
import pe.edu.utp.farmacia.util.JwtUtil;

import static org.junit.jupiter.api.Assertions.*;

public class AuthResourceTest {

    private final UsuarioFarmaciaService usuarioService = new UsuarioFarmaciaService();

    @Test
    @DisplayName("Prueba de Servicio y Autenticación de Login con Generación de JWT")
    public void testAutenticarYGenerarTokenJWT() throws Exception {
        long timestamp = System.currentTimeMillis();
        String username = "adm_" + (timestamp % 100000);

        UsuarioFarmacia u = new UsuarioFarmacia();
        u.setUsername(username);
        u.setNombres("Admin");
        u.setApellidos("Auth REST");
        u.setRol("ADMINISTRADOR");
        usuarioService.registrar(u, "Farmacia2026*", "test_runner");

        UsuarioFarmacia usuario = usuarioService.autenticar(username, "Farmacia2026*");
        assertNotNull(usuario, "El usuario registrado debe autenticarse correctamente con su clave BCrypt");
        assertEquals(username, usuario.getUsername());
        assertEquals("ADMINISTRADOR", usuario.getRol());

        String token = JwtUtil.generarToken(usuario.getUsername(), usuario.getRol(), usuario.getIdUsuario());
        assertNotNull(token, "El token JWT generado no debe ser nulo");
        assertFalse(token.isBlank(), "El token JWT generado no debe estar vacío");

        assertTrue(JwtUtil.esTokenValido(token), "El token JWT debe ser validado como verdadero");
        assertEquals(username, JwtUtil.obtenerUsername(token), "El username extraído del token debe coincidir");
    }

    @Test
    @DisplayName("Prueba de Autenticación Fallida con Clave Incorrecta")
    public void testAutenticacionFallida() throws Exception {
        UsuarioFarmacia usuario = usuarioService.autenticar("admin_inexistente_12345", "ClaveIncorrecta123");
        assertNull(usuario, "La autenticación debe retornar null ante credenciales inválidas");
    }
}
