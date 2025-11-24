package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.PublicationModel;
import com.example.demo.service.PublicationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/publicaciones")

public class PublicationController {
    @Autowired
    private PublicationService publicationService;


    @Operation(summary = "Crear una nueva publicacion ")
    @ApiResponse(responseCode = "200", description = "Publicacion creada exitosamente",
        content=@Content(schema = @Schema(implementation = PublicationModel.class)))
    @ApiResponse(responseCode = "400", description = "Error al crear la publicacion")
     @PostMapping("/publicar")
    public ResponseEntity<?> crearPubli(@RequestBody PublicationModel publi) {
        try {
            // Ignorar cualquier id que venga del cliente para forzar INSERT
            // Asegúrate de que PublicationModel tenga setId(Long id)
            publi.setId(null);

            PublicationModel savedPublication = publicationService.crearPublicacion(publi);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPublication);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    @Operation(summary = "Obtener todas las publicaciones")
    @ApiResponse(responseCode = "200", description = "Lista de publicaciones",
        content=@Content(schema = @Schema(implementation = PublicationModel.class)))
    @ApiResponse(responseCode = "204", description = "No hay publicaciones")
    public ResponseEntity<List<PublicationModel>> obtenerTodasPubli() {
        List<PublicationModel> publicaciones = publicationService.obtenerTodasPubli();
        return new ResponseEntity<>(publicaciones, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener publicación por ID")
    @ApiResponse(responseCode = "200", description = "Publicación encontrada",
        content=@Content(schema = @Schema(implementation = PublicationModel.class)))
    @ApiResponse(responseCode = "404", description = "Publicación no encontrada")
    public ResponseEntity<?> obtenerPorId(@PathVariable("id") Long id) {
        try {
            PublicationModel pub = publicationService.buscarPublicacionPorId(id);
            return ResponseEntity.ok(pub);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/usuario")
    @Operation(summary = "Obtener publicaciones por usuario")
    @ApiResponse(responseCode = "200", description = "Publicaciones del usuario",
        content=@Content(schema = @Schema(implementation = PublicationModel.class)))
    @ApiResponse(responseCode = "204", description = "No hay publicaciones para el usuario")
    public ResponseEntity<List<PublicationModel>> obtenerPubliPorUsuario(Long userid) {
        List<PublicationModel> publicaciones = publicationService.obtenerPubliPorUsuario(userid);
        return new ResponseEntity<>(publicaciones, HttpStatus.OK);
    }

    @DeleteMapping("/{publicationId}")
    @Operation(summary = "Eliminar publicación por ID")
    @ApiResponse(responseCode = "200", description = "Publicación eliminada correctamente",
        content=@Content(schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "404", description = "Publicación no encontrada")
    public ResponseEntity<?> eliminarPubli(@PathVariable("publicationId") Long publicationId) {
        try {
            // debug opcional
            System.out.println(">>> DELETE called con id = " + publicationId);

            publicationService.eliminarPubli(publicationId);
            return ResponseEntity.ok("Publicación eliminada correctamente");
        } catch (RuntimeException e) {
            // si es autorización devolvemos 403, si no 404/400 según prefieras
            String msg = e.getMessage();
            if (msg != null && msg.toLowerCase().contains("no autorizado")) {
                return ResponseEntity.status(403).body(msg);
            }
            return ResponseEntity.status(404).body(msg);
        }
    }

}
