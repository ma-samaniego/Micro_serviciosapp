package com.example.comentarios.service;

import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.comentarios.model.ComentarioModel;
import com.example.comentarios.repository.ComentarioRepository;
import com.example.comentarios.webClient.UsuarioClient;
import com.example.comentarios.webClient.PublicacionClient;

@Service
public class ComentarioService { // <-- Nombre Corregido

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private UsuarioClient usuarioClient;

    @Autowired
    private PublicacionClient publicacionClient; // Cliente para validar publicaciones

    public ComentarioModel crearComentario(ComentarioModel comentario) {
        // 1. Validar Usuario
        Map<String, Object> usuarioData = usuarioClient.getUsuarioById(comentario.getUsuarioId());
        if (usuarioData == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        // 2. Validar Publicación
        try {
            publicacionClient.getPublicacionById(comentario.getPublicationId());
        } catch (Exception e) {
             throw new RuntimeException("La publicación no existe o no se pudo verificar.");
        }

        // Si no viene fecha de creación, asignar ahora en formato ISO
        if (comentario.getFechaCreacion() == null || comentario.getFechaCreacion().isBlank()) {
            String now = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            comentario.setFechaCreacion(now);
        }

        return comentarioRepository.save(comentario);
    }

    public List<ComentarioModel> obtenerComentariosPorPublicacion(Long publicationId) {
        return comentarioRepository.findByPublicationId(publicationId);
    }
    
    public List<ComentarioModel> obtenerComentariosPorUsuario(Long usuarioId) {
        return comentarioRepository.findByUsuarioId(usuarioId);
    }

    public void eliminarComentario(Long comentarioId) {
         // Lógica simplificada para el ejemplo, idealmente validas rol ADMIN igual que en publicaciones
         if (!comentarioRepository.existsById(comentarioId)) {
             throw new RuntimeException("Comentario no encontrado");
         }
         comentarioRepository.deleteById(comentarioId);
    }
}