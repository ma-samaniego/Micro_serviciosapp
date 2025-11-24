package Iniciosecion.Iniciosecion.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import Iniciosecion.Iniciosecion.dto.LoginResponse;
import Iniciosecion.Iniciosecion.dto.UsuarioDTO;
import Iniciosecion.Iniciosecion.model.EstadoUsuario;
import Iniciosecion.Iniciosecion.model.Rol;
import Iniciosecion.Iniciosecion.model.Usuario;
import Iniciosecion.Iniciosecion.repository.RoleRepository;
import Iniciosecion.Iniciosecion.repository.UsuarioRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional


public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;//para encriptar la contrase��a llamando del config SeguritiConfig

    public List<UsuarioDTO> buscarUsuario(){
        return usuarioRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
        
    }

    private UsuarioDTO convertirADTO(Usuario usuario){
        UsuarioDTO usuarioDTO= new UsuarioDTO();
        usuarioDTO.setUsuario_id(usuario.getUsuario_id());
        usuarioDTO.setNombre_usuario(usuario.getNombre_usuario());
        usuarioDTO.setNumero_telefono(usuario.getNumero_telefono());
        usuarioDTO.setCorreo(usuario.getCorreo());
        usuarioDTO.setEstado(usuario.getEstado());
        usuarioDTO.setRol_id(usuario.getRol().getRol_id());
        return usuarioDTO;
    }

    public Usuario creaerUsuario(String nombre_usuario, String contrasena,String  numero_telefonok, String correo, EstadoUsuario estado, Long rol_id){
        if(contrasena== null || contrasena.isBlank()){
            throw new RuntimeException("La contraseña no puede esatr vacia  o nula");
        }
        Rol role= roleRepository.findById(rol_id).orElseThrow(()-> new RuntimeException("Rol no encontrado"));

        Usuario user= new Usuario();
        user.setContrasena(passwordEncoder.encode(contrasena));//encriptamos la contraseña
        user.setCorreo(correo);
        user.setEstado(estado);
        user.setNombre_usuario(nombre_usuario);
        user.setNumero_telefono(numero_telefonok);
        user.setRol(role);
        return usuarioRepository.save(user);

        
    }
    public void eliminarUsuario(Long usuario_id){
        Usuario usuario=usuarioRepository.findById(usuario_id).orElseThrow(()-> new RuntimeException("Usuario no encontrado"));
         usuarioRepository.delete(usuario);
    }
    public Usuario buscarporid(Long usuario_id){
        return usuarioRepository.findById(usuario_id).orElseThrow(()-> new RuntimeException("Usuario no encontrado"));
    }

    public Usuario actualizarUsuario(Long usuario_id, Usuario usuarioActualizado){
        Usuario usuarioExiste = usuarioRepository.findById(usuario_id).orElseThrow(()-> new RuntimeException("Usuario no encontrado"));
        // Actualizar solo los campos enviados (evitar sobrescribir con null)
        if (usuarioActualizado.getNombre_usuario() != null) {
            usuarioExiste.setNombre_usuario(usuarioActualizado.getNombre_usuario());
        }

        if (usuarioActualizado.getNumero_telefono() != null) {
            usuarioExiste.setNumero_telefono(usuarioActualizado.getNumero_telefono());
        }

        if (usuarioActualizado.getCorreo() != null) {
            usuarioExiste.setCorreo(usuarioActualizado.getCorreo());
        }

        if (usuarioActualizado.getEstado() != null) {
            usuarioExiste.setEstado(usuarioActualizado.getEstado());
        }

        // Manejar la contraseña: solo actualizar si se envía una nueva contraseña no vacía
        if (usuarioActualizado.getContrasena() != null && !usuarioActualizado.getContrasena().isBlank()) {
            usuarioExiste.setContrasena(passwordEncoder.encode(usuarioActualizado.getContrasena()));
        }

        // Manejar rol: si se proporciona un rol con id, validar que exista y asignarlo
        if (usuarioActualizado.getRol() != null && usuarioActualizado.getRol().getRol_id() != null) {
            Rol role = roleRepository.findById(usuarioActualizado.getRol().getRol_id())
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
            usuarioExiste.setRol(role);
        }

        return usuarioRepository.save(usuarioExiste);
    }

    public LoginResponse autenticarUsuario(String nombre_usuario, String contrasena){
        Usuario usuario= usuarioRepository.findAll()
                .stream()
                .filter(u-> u.getNombre_usuario().equals(nombre_usuario))
                .findFirst()
                .orElseThrow(()-> new RuntimeException("Usuario no encontrado"));

        if(!passwordEncoder.matches(contrasena, usuario.getContrasena())){
            throw new RuntimeException("Contraseña incorrecta");
        }

        LoginResponse loginResponse= new LoginResponse();
        loginResponse.setUsuario_id(usuario.getUsuario_id());
        loginResponse.setNombre_usuario(usuario.getNombre_usuario());
        loginResponse.setCorreo(usuario.getCorreo());
        loginResponse.setEstado(usuario.getEstado());
        loginResponse.setRol_id(usuario.getRol().getRol_id());

        return loginResponse;
    }



    

}
