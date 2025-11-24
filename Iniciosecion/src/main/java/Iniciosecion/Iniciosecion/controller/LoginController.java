package Iniciosecion.Iniciosecion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Iniciosecion.Iniciosecion.dto.LoginRequest;
import Iniciosecion.Iniciosecion.dto.LoginResponse;
import Iniciosecion.Iniciosecion.service.UsuarioService;
import Iniciosecion.Iniciosecion.config.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/v1/auth")
public class LoginController {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private JwtUtil jwtUtil;

	@Operation(summary = "Autenticar usuario")
	@ApiResponse(responseCode = "200", description = "Usuario autenticado exitosamente",
		content = @Content(schema = @Schema(implementation = LoginResponse.class)))
	@ApiResponse(responseCode = "400", description = "Credenciales inválidas")
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest request) {
		try {
			LoginResponse response = usuarioService.autenticarUsuario(request.getNombreUsuario(), request.getContrasena());
			String token = jwtUtil.generateToken(response.getNombre_usuario(), response.getRol_id());
			response.setToken(token);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

}
