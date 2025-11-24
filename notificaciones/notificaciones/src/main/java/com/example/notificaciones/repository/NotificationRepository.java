package com.example.notificaciones.repository;
import com.example.notificaciones.model.NotificationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationModel, Long> {
    
    // Buscar todas las notificaciones de un usuario, ordenadas por fecha (más nuevas primero)
    List<NotificationModel> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    // Contar no leídas (útil para badges en la app)
    Long countByUserIdAndIsReadFalse(Long userId);
}