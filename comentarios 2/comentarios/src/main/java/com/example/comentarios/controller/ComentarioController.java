package com.example.comentarios.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.comentarios.model.ComentarioModel;
import com.example.comentarios.service.ComentarioService;

@RestController
@RequestMapping("/api/comentarios")
public class ComentarioController { // <-- Nombre Corregido

    @Autowired
    private ComentarioService comentarioService;

    @PostMapping("/comentar")
    public ResponseEntity<?> crearComentario(@RequestBody ComentarioModel comentario) {
        try {
            ComentarioModel saved = comentarioService.crearComentario(comentario);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/publicacion/{publicationId}")
    public ResponseEntity<List<ComentarioModel>> obtenerPorPublicacion(@PathVariable Long publicationId) {
        return ResponseEntity.ok(comentarioService.obtenerComentariosPorPublicacion(publicationId));
    }
    
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ComentarioModel>> obtenerPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(comentarioService.obtenerComentariosPorUsuario(usuarioId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarComentario(@PathVariable Long id) {
        try {
            comentarioService.eliminarComentario(id);
            return ResponseEntity.ok("Comentario eliminado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}