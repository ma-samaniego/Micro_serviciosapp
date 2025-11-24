package Iniciosecion.Iniciosecion.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.annotation.Generated;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rol_id;
    
    @Column(nullable = false)
    private String permisos;
    
    @Column(nullable = false)
    private String nombre_rol;

    //para que se relacione con elusuario 
    @OneToMany(mappedBy = "rol" , cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Usuario> usuarios;


}
