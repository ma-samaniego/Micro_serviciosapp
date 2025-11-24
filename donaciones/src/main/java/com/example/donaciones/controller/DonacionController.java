package com.example.donaciones.controller;

import com.example.donaciones.model.DonacionModel;
import com.example.donaciones.service.DonacionService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donaciones")
public class DonacionController {

    @Autowired
    private DonacionService donacionService;

    @Operation(summary = "Realizar una nueva donación")
    @PostMapping
    public ResponseEntity<?> donar(@RequestBody DonacionModel donacion) {
        try {
            DonacionModel nuevaDonacion = donacionService.crearDonacion(donacion);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaDonacion);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Ver donaciones realizadas por un usuario")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<DonacionModel>> verDonacionesUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(donacionService.obtenerPorUsuario(usuarioId));
    }

    @Operation(summary = "Ver donaciones recibidas en una publicación")
    @GetMapping("/publicacion/{publicationId}")
    public ResponseEntity<List<DonacionModel>> verDonacionesPublicacion(@PathVariable Long publicationId) {
        return ResponseEntity.ok(donacionService.obtenerPorPublicacion(publicationId));
    }
}