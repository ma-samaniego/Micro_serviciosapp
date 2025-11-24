package com.example.notificaciones.controller;

import com.example.notificaciones.model.NotificationModel;
import com.example.notificaciones.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void crearNotificacion_returnsSaved() throws Exception {
        NotificationModel sample = new NotificationModel(1L, 2L, "admin", "motivo", "titulo", LocalDateTime.now().toString(), false);
        when(notificationService.crearNotificacion(any(NotificationModel.class))).thenReturn(sample);

        mockMvc.perform(post("/api/notificaciones/notificar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sample)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(sample)));
    }

    @Test
    void obtenerPorUsuario_returnsListOrNoContent() throws Exception {
        NotificationModel sample = new NotificationModel(1L, 2L, "admin", "motivo", "titulo", LocalDateTime.now().toString(), false);
        when(notificationService.obtenerNotificacionesPorUsuario(2L)).thenReturn(Arrays.asList(sample));

        mockMvc.perform(get("/api/notificaciones/usuario/2"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(sample))));

        when(notificationService.obtenerNotificacionesPorUsuario(3L)).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/notificaciones/usuario/3"))
                .andExpect(status().isNoContent());
    }

    @Test
    void marcarLeida_callsServiceAndReturnsOk() throws Exception {
        Mockito.doNothing().when(notificationService).marcarComoLeida(1L);

        mockMvc.perform(put("/api/notificaciones/1/leer"))
                .andExpect(status().isOk());
    }
}
