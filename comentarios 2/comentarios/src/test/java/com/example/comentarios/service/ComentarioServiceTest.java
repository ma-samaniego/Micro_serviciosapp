package com.example.comentarios.service;

import com.example.comentarios.model.ComentarioModel;
import com.example.comentarios.repository.ComentarioRepository;
import com.example.comentarios.webClient.PublicacionClient;
import com.example.comentarios.webClient.UsuarioClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComentarioServiceTest {

    @Mock
    private ComentarioRepository comentarioRepository;

    @Mock
    private UsuarioClient usuarioClient;

    @Mock
    private PublicacionClient publicacionClient;

    @InjectMocks
    private ComentarioService comentarioService;

    private ComentarioModel sample;

    @BeforeEach
    void setUp() {
        sample = new ComentarioModel();
        sample.setId(1L);
        sample.setPublicationId(10L);
        sample.setUsuarioId(20L);
        sample.setContenido("Contenido de prueba");
        sample.setAutorNombre("Autor");
        sample.setFechaCreacion("");
    }

    @Test
    void crearComentario_success_savesAndReturns() {
        when(usuarioClient.getUsuarioById(20L)).thenReturn(Map.of("id", 20L));
        when(publicacionClient.getPublicacionById(10L)).thenReturn(Map.of("id", 10L));
        when(comentarioRepository.save(any(ComentarioModel.class))).thenAnswer(i -> i.getArgument(0));

        ComentarioModel result = comentarioService.crearComentario(sample);

        assertThat(result.getPublicationId()).isEqualTo(10L);
        assertThat(result.getUsuarioId()).isEqualTo(20L);
        assertThat(result.getFechaCreacion()).isNotBlank();
        verify(comentarioRepository, times(1)).save(any(ComentarioModel.class));
    }

    @Test
    void crearComentario_usuarioNotFound_throws() {
        when(usuarioClient.getUsuarioById(20L)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> comentarioService.crearComentario(sample));
        verify(comentarioRepository, never()).save(any());
    }

    @Test
    void crearComentario_publicacionNotFound_throws() {
        when(usuarioClient.getUsuarioById(20L)).thenReturn(Map.of("id", 20L));
        when(publicacionClient.getPublicacionById(10L)).thenThrow(new RuntimeException("no existe"));

        assertThrows(RuntimeException.class, () -> comentarioService.crearComentario(sample));
        verify(comentarioRepository, never()).save(any());
    }

    @Test
    void obtenerComentariosPorPublicacion_returnsList() {
        when(comentarioRepository.findByPublicationId(10L)).thenReturn(asList(sample));

        List<ComentarioModel> list = comentarioService.obtenerComentariosPorPublicacion(10L);

        assertThat(list).hasSize(1);
        assertThat(list.get(0).getPublicationId()).isEqualTo(10L);
    }

    @Test
    void obtenerComentariosPorUsuario_returnsList() {
        when(comentarioRepository.findByUsuarioId(20L)).thenReturn(asList(sample));

        List<ComentarioModel> list = comentarioService.obtenerComentariosPorUsuario(20L);

        assertThat(list).hasSize(1);
        assertThat(list.get(0).getUsuarioId()).isEqualTo(20L);
    }

    @Test
    void eliminarComentario_exists_deletes() {
        when(comentarioRepository.existsById(1L)).thenReturn(true);

        comentarioService.eliminarComentario(1L);

        verify(comentarioRepository).deleteById(1L);
    }

    @Test
    void eliminarComentario_notFound_throws() {
        when(comentarioRepository.existsById(99L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> comentarioService.eliminarComentario(99L));
        verify(comentarioRepository, never()).deleteById(any());
    }
}
