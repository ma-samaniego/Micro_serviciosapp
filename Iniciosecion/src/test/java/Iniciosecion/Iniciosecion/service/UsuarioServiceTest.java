package Iniciosecion.Iniciosecion.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import Iniciosecion.Iniciosecion.dto.LoginResponse;
import Iniciosecion.Iniciosecion.model.EstadoUsuario;
import Iniciosecion.Iniciosecion.model.Rol;
import Iniciosecion.Iniciosecion.model.Usuario;
import Iniciosecion.Iniciosecion.repository.RoleRepository;
import Iniciosecion.Iniciosecion.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new Usuario();
        sampleUser.setUsuario_id(1L);
        sampleUser.setNombre_usuario("juanperez");
        sampleUser.setContrasena("$2a$10$encrypted"); // simulated encoded password
        sampleUser.setCorreo("juan.perez@example.com");
        sampleUser.setNumero_telefono("123456789");
        sampleUser.setEstado(EstadoUsuario.ACTIVO);
        Rol rol = new Rol();
        rol.setRol_id(2L);
        rol.setNombre_rol("USER");
        rol.setPermisos("READ");
        sampleUser.setRol(rol);
    }

    @Test
    void autenticarUsuario_Success() {
        when(usuarioRepository.findAll()).thenReturn(List.of(sampleUser));
        when(passwordEncoder.matches("miPassword123", sampleUser.getContrasena())).thenReturn(true);

        LoginResponse resp = usuarioService.autenticarUsuario("juanperez", "miPassword123");

        assertNotNull(resp);
        assertEquals(1L, resp.getUsuario_id());
        assertEquals("juanperez", resp.getNombre_usuario());
        assertEquals("juan.perez@example.com", resp.getCorreo());
        assertEquals(EstadoUsuario.ACTIVO, resp.getEstado());
        assertEquals(2L, resp.getRol_id());
    }

    @Test
    void autenticarUsuario_WrongPassword_Throws() {
        when(usuarioRepository.findAll()).thenReturn(List.of(sampleUser));
        when(passwordEncoder.matches("wrong", sampleUser.getContrasena())).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
            usuarioService.autenticarUsuario("juanperez", "wrong")
        );
        assertTrue(ex.getMessage().contains("Contraseña incorrecta"));
    }

    @Test
    void autenticarUsuario_UserNotFound_Throws() {
        when(usuarioRepository.findAll()).thenReturn(List.of());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
            usuarioService.autenticarUsuario("noexiste", "pass")
        );
        assertTrue(ex.getMessage().contains("Usuario no encontrado"));
    }
}
