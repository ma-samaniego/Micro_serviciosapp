package Iniciosecion.Iniciosecion.model;

import org.hibernate.annotations.ManyToAny;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long usuario_id;

    @Column(nullable = false)
    private String nombre_usuario;

    @Column(nullable = false)
    private String contrasena;

    @Column(nullable = false)
    private String numero_telefono;

    @Column(nullable = false)
    private String correo;

    // usaremos enum en este caso lo definiremos en otra parte ya que aqui no se
    // puede
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoUsuario estado;

    @ManyToOne
    @JoinColumn(name = "rol_id", nullable = false)
    @JsonIgnoreProperties("usuarios")
    private Rol rol;


}
