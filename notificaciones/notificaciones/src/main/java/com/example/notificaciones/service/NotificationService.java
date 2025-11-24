package com.example.notificaciones.service;

import com.example.notificaciones.model.NotificationModel;
import com.example.notificaciones.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private NotificationRepository notificationRepository;

    // Crear una notificación (llamado cuando se borra un post)
    @Transactional
    public NotificationModel crearNotificacion(NotificationModel notificacion) {
        if (notificacion == null) {
            throw new IllegalArgumentException("Notificación nula");
        }

        // Intentamos forzar INSERT: ignorar id enviado por el cliente si existe
        try {
            notificacion.setId(null);
        } catch (Exception e) {
            // Si el modelo no tiene setId o ID es primitivo, ignoramos
            log.debug("No se pudo forzar id=null en NotificationModel (método ausente o primitivo)");
        }

        // Si no viene createdAt, lo generamos en el servidor
        try {
            if (notificacion.getCreatedAt() == null) {
                // NotificationModel espera un String para createdAt en este proyecto
                notificacion.setCreatedAt(LocalDateTime.now().toString());
            }
        } catch (Exception e) {
            log.debug("getCreatedAt/setCreatedAt no están disponibles en NotificationModel: {}", e.getMessage());
        }

        // Asegurar isRead por defecto si el modelo lo permite
        try {
            if (notificacion.getIsRead() == null) {
                notificacion.setIsRead(false);
            }
        } catch (Exception e) {
            // puede ser primitivo boolean o no existir; no es crítico
        }

        log.debug("Guardando notificación para userId={} publicationTitle={}",
                safeUserId(notificacion), safePublicationTitle(notificacion));

        return notificationRepository.save(notificacion);
    }

    // Obtener notificaciones para un usuario
    public List<NotificationModel> obtenerNotificacionesPorUsuario(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    // Marcar como leída (opcional, para cuando el usuario abre la notificación)
    public void marcarComoLeida(Long notificationId) {
        if (notificationId == null) throw new IllegalArgumentException("notificationId nulo");
        NotificationModel noti = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));
        noti.setIsRead(true);
        notificationRepository.save(noti);
    }

    // Helpers seguros para logging (evitan NPE si getters ausentes)
    private Object safeUserId(NotificationModel n) {
        try { return n.getUserId(); } catch (Exception e) { return "?"; }
    }

    private Object safePublicationTitle(NotificationModel n) {
        try { return n.getPublicationTitle(); } catch (Exception e) { return "?"; }
    }
}