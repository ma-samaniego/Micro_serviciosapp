package com.example.comentarios.model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonAlias;

@Entity
@Data
@Table(name = "comentario")
@NoArgsConstructor
@AllArgsConstructor
public class ComentarioModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long publicationId;

    @Column(nullable = false)
    @JsonAlias({"userId", "user_id"})
    private Long usuarioId;

    @Column(nullable = false, length = 1000)
    @JsonAlias({"text", "message", "body"})
    private String contenido;

    @Column(nullable = false)
    @JsonAlias({"authorName", "author_name", "author"})
    private String autorNombre;

    @Column(nullable = false)
    private String fechaCreacion;
}