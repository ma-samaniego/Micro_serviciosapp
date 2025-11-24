package com.example.demo.controller;

import com.example.demo.model.PublicationModel;
import com.example.demo.service.PublicationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PublicationController.class)
@WithMockUser
class PublicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PublicationService publicationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void crearPubli_returnsCreated() throws Exception {
        PublicationModel sample = new PublicationModel(null, 20L, "cat", "img.jpg", "title", "desc", "author", "2025-01-01T00:00:00", "ACTIVE", 0);
        PublicationModel saved = new PublicationModel(1L, 20L, "cat", "img.jpg", "title", "desc", "author", "2025-01-01T00:00:00", "ACTIVE", 0);
        when(publicationService.crearPublicacion(any(PublicationModel.class))).thenReturn(saved);

        mockMvc.perform(post("/api/publicaciones/publicar").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sample)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(saved)));
    }

    @Test
    void obtenerTodas_returnsOk() throws Exception {
        PublicationModel sample = new PublicationModel(1L, 20L, "cat", "img.jpg", "title", "desc", "author", "2025-01-01T00:00:00", "ACTIVE", 0);
        when(publicationService.obtenerTodasPubli()).thenReturn(Arrays.asList(sample));

        mockMvc.perform(get("/api/publicaciones").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(sample))));
    }

    @Test
    void obtenerPorId_found_returnsOk() throws Exception {
        PublicationModel sample = new PublicationModel(1L, 20L, "cat", "img.jpg", "title", "desc", "author", "2025-01-01T00:00:00", "ACTIVE", 0);
        when(publicationService.buscarPublicacionPorId(1L)).thenReturn(sample);

        mockMvc.perform(get("/api/publicaciones/1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(sample)));
    }

    @Test
    void eliminarPubli_exists_returnsOk() throws Exception {
        doNothing().when(publicationService).eliminarPubli(1L);

        mockMvc.perform(delete("/api/publicaciones/1").with(csrf()))
                .andExpect(status().isOk());
    }
}
