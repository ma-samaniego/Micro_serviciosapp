package Iniciosecion.Iniciosecion.dto;

import Iniciosecion.Iniciosecion.model.EstadoUsuario;
import lombok.Data;

@Data
public class LoginResponse {
    private Long usuario_id;
    private String nombre_usuario;
    private String correo;
    private EstadoUsuario estado;
    private Long rol_id;
    private String token;

}
