package Iniciosecion.Iniciosecion.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import Iniciosecion.Iniciosecion.config.JwtUtil;
import Iniciosecion.Iniciosecion.dto.LoginRequest;
import Iniciosecion.Iniciosecion.dto.LoginResponse;
import Iniciosecion.Iniciosecion.service.UsuarioService;

@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private LoginController loginController;

    private LoginRequest request;

    @BeforeEach
    void setUp() {
        request = new LoginRequest();
        request.setNombreUsuario("juanperez");
        request.setContrasena("miPassword123");
    }

    @Test
    void login_Success_ReturnsToken() {
        LoginResponse loginResp = new LoginResponse();
        loginResp.setUsuario_id(1L);
        loginResp.setNombre_usuario("juanperez");
        loginResp.setCorreo("juan.perez@example.com");
        loginResp.setEstado(null);
        loginResp.setRol_id(2L);

        when(usuarioService.autenticarUsuario("juanperez", "miPassword123")).thenReturn(loginResp);
        when(jwtUtil.generateToken("juanperez", 2L)).thenReturn("token123");

        ResponseEntity<?> respEntity = loginController.login(request);

        assertEquals(200, respEntity.getStatusCodeValue());
        assertTrue(respEntity.getBody() instanceof LoginResponse);
        LoginResponse body = (LoginResponse) respEntity.getBody();
        assertEquals("token123", body.getToken());
        assertEquals(1L, body.getUsuario_id());
    }

    @Test
    void login_Failure_ReturnsBadRequest() {
        when(usuarioService.autenticarUsuario("juanperez", "miPassword123")).thenThrow(new RuntimeException("Usuario no encontrado"));

        ResponseEntity<?> respEntity = loginController.login(request);

        assertEquals(400, respEntity.getStatusCodeValue());
        assertTrue(respEntity.getBody() instanceof String);
        assertEquals("Usuario no encontrado", respEntity.getBody());
    }
}
