package com.example.notificaciones.controller;

import com.example.notificaciones.model.NotificationModel;
import com.example.notificaciones.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/notificar")
    public ResponseEntity<NotificationModel> crearNotificacion(@RequestBody NotificationModel notificacion) {
        return ResponseEntity.ok(notificationService.crearNotificacion(notificacion));
    }

    @GetMapping("/usuario/{userId}")
    public ResponseEntity<List<NotificationModel>> obtenerPorUsuario(@PathVariable Long userId) {
        List<NotificationModel> notificaciones = notificationService.obtenerNotificacionesPorUsuario(userId);
        if (notificaciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(notificaciones);
    }
    
    @PutMapping("/{id}/leer")
    public ResponseEntity<Void> marcarLeida(@PathVariable Long id) {
        notificationService.marcarComoLeida(id);
        return ResponseEntity.ok().build();
    }
}