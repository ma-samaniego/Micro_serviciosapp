package Iniciosecion.Iniciosecion.dto;

import lombok.Data;

@Data

public class UsuarioDTO {
    private Long usuario_id;
    private String nombre_usuario;
    private String contrasena;
    private String numero_telefono;
    private String correo;
    private Enum estado;
    private Long rol_id;

   
   

}
