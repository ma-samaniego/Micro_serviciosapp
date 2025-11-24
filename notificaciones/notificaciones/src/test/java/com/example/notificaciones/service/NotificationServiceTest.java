package com.example.notificaciones.service;

import com.example.notificaciones.model.NotificationModel;
import com.example.notificaciones.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    private NotificationModel sample;

    @BeforeEach
    void setUp() {
        sample = new NotificationModel();
        sample.setId(1L);
        sample.setUserId(2L);
        sample.setAdminName("admin");
        sample.setMessage("Motivo de ejemplo");
        sample.setPublicationTitle("Titulo de prueba");
        sample.setCreatedAt(LocalDateTime.now().toString());
        sample.setIsRead(false);
    }

    @Test
    void crearNotificacion_callsRepositorySave_andReturnsSaved() {
        when(notificationRepository.save(any(NotificationModel.class))).thenReturn(sample);

        NotificationModel result = notificationService.crearNotificacion(sample);

        assertThat(result).isEqualTo(sample);
        verify(notificationRepository, times(1)).save(sample);
    }

    @Test
    void obtenerNotificacionesPorUsuario_returnsListFromRepository() {
        when(notificationRepository.findByUserIdOrderByCreatedAtDesc(2L)).thenReturn(asList(sample));

        List<NotificationModel> list = notificationService.obtenerNotificacionesPorUsuario(2L);

        assertThat(list).hasSize(1);
        assertThat(list.get(0).getUserId()).isEqualTo(2L);
        verify(notificationRepository).findByUserIdOrderByCreatedAtDesc(2L);
    }

    @Test
    void marcarComoLeida_updatesIsReadAndSaves() {
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(sample));
        when(notificationRepository.save(any(NotificationModel.class))).thenAnswer(i -> i.getArgument(0));

        notificationService.marcarComoLeida(1L);

        ArgumentCaptor<NotificationModel> captor = ArgumentCaptor.forClass(NotificationModel.class);
        verify(notificationRepository).save(captor.capture());
        NotificationModel saved = captor.getValue();
        assertThat(saved.getIsRead()).isTrue();
    }

    @Test
    void marcarComoLeida_whenNotFound_throwsRuntimeException() {
        when(notificationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> notificationService.marcarComoLeida(99L));
        verify(notificationRepository, never()).save(any());
    }
}
