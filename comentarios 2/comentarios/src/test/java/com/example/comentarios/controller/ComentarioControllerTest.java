package com.example.comentarios.controller;

import com.example.comentarios.model.ComentarioModel;
import com.example.comentarios.service.ComentarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(ComentarioController.class)
@WithMockUser
class ComentarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ComentarioService comentarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void crearComentario_returnsCreated() throws Exception {
        ComentarioModel sample = new ComentarioModel(1L, 10L, 20L, "contenido", "autor", "2025-01-01T00:00:00");
        when(comentarioService.crearComentario(any(ComentarioModel.class))).thenReturn(sample);

        mockMvc.perform(post("/api/comentarios/comentar").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(sample)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(sample)));
    }

    @Test
    void obtenerPorPublicacion_returnsList() throws Exception {
        ComentarioModel sample = new ComentarioModel(1L, 10L, 20L, "contenido", "autor", "2025-01-01T00:00:00");
        when(comentarioService.obtenerComentariosPorPublicacion(10L)).thenReturn(Arrays.asList(sample));

        mockMvc.perform(get("/api/comentarios/publicacion/10"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(sample))));
    }

    @Test
    void obtenerPorUsuario_returnsList() throws Exception {
        ComentarioModel sample = new ComentarioModel(1L, 10L, 20L, "contenido", "autor", "2025-01-01T00:00:00");
        when(comentarioService.obtenerComentariosPorUsuario(20L)).thenReturn(Arrays.asList(sample));

        mockMvc.perform(get("/api/comentarios/usuario/20"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(sample))));
    }

    @Test
    void eliminarComentario_exists_returnsOk() throws Exception {
        doNothing().when(comentarioService).eliminarComentario(1L);

        mockMvc.perform(delete("/api/comentarios/1").with(csrf()))
                .andExpect(status().isOk());
    }
}
