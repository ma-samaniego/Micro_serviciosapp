package com.example.notificaciones.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "notificaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId; // El usuario que RECIBE la notificación (dueño del post)

    @Column(nullable = false)
    private String adminName; // El nombre del admin que borró el post

    @Column(nullable = false)
    private String message; // El motivo del borrado

    @Column(nullable = false)
    private String publicationTitle; // Título del post borrado (para contexto)

    @Column(nullable = false, updatable = false)

    private String createdAt;

    @Column(nullable = false)
    private Boolean isRead = false; // Para saber si ya la leyó (opcional, útil a futuro)


}