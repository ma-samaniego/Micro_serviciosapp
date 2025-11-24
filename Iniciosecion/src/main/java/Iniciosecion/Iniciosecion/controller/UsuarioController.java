package Iniciosecion.Iniciosecion.controller;

import java.util.List;

import javax.management.relation.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Iniciosecion.Iniciosecion.dto.UsuarioDTO;
import Iniciosecion.Iniciosecion.model.Rol;
import Iniciosecion.Iniciosecion.model.Usuario;
import Iniciosecion.Iniciosecion.service.RoleService;
import Iniciosecion.Iniciosecion.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/v1")

public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private RoleService roleService;

    @Operation(summary = "Crear una nueva publicacion ")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios",
        content=@Content(schema = @Schema(implementation = UsuarioDTO.class)))
    @ApiResponse(responseCode = "400", description = "Error en la solicitud",
        content=@Content)
    @GetMapping("/user")
    public List<UsuarioDTO>  listarUsuarios(){
        return usuarioService.buscarUsuario();
    }
    
    @Operation(summary = "Obtener lista de roles")
    @ApiResponse(responseCode = "200", description = "Lista de roles",
        content=@Content(schema = @Schema(implementation = Rol.class)))
    @ApiResponse(responseCode = "400", description = "Error en la solicitud",
        content=@Content)
    @GetMapping("/roles")
    public ResponseEntity<List<Rol>> obtenerRoles(){
        List <Rol> roles= roleService.buscarRol();
         return roles.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(roles);
        
    }

    @PostMapping("/users")
    @Operation(summary = "Crear un nuevo usuario")
    @ApiResponse(responseCode = "200", description = "Usuario creado exitosamente",
        content=@Content(schema = @Schema(implementation = Usuario.class)))
    @ApiResponse(responseCode = "400", description = "Error al crear el usuario")
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario){
        try{
            System.out.println("Contraseña rescibida "+ usuario.getContrasena());

            Long rol_id= usuario.getRol().getRol_id();
            Usuario nuevousuario = usuarioService.creaerUsuario(
                usuario.getNombre_usuario(),
                usuario.getContrasena(),
                usuario.getNumero_telefono(),
                usuario.getCorreo(),
                usuario.getEstado(),
                rol_id
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevousuario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        
    }
    @DeleteMapping("/users/{usuario_id}")
    @Operation(summary = "Eliminar un usuario por ID")
    @ApiResponse(responseCode = "200", description = "Usuario eliminado correctamente",
        content=@Content(schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long usuario_id){
        try {
            usuarioService.eliminarUsuario(usuario_id);
            return ResponseEntity.ok("Usuario eliminado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    @GetMapping("/users/{usuario_id}")
    @Operation(summary = "Obtener usuario por ID")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado",
        content=@Content(schema = @Schema(implementation = Usuario.class)))
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    public ResponseEntity<?> buscarporID(@PathVariable Long usuario_id){
        try {
            Usuario usuario= usuarioService.buscarporid(usuario_id);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @PutMapping("/users/{usuario_id}")
    @Operation(summary = "Actualizar usuario por ID")
    @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente",
        content=@Content(schema = @Schema(implementation = Usuario.class)))
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long usuario_id, @RequestBody Usuario usuario){
        try {
            Usuario usuarioActualizado= usuarioService.actualizarUsuario(usuario_id, usuario);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
