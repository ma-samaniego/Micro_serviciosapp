package com.example.demo.service;

import com.example.demo.model.PublicationModel;
import com.example.demo.repository.PublicationRepository;
import com.example.demo.webClient.UsuarioClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PublicationServiceTest {

    @Mock
    private PublicationRepository publicationRepository;

    @Mock
    private UsuarioClient usuarioClient;

    @InjectMocks
    private PublicationService publicationService;

    private PublicationModel sample;

    @BeforeEach
    void setUp() {
        sample = new PublicationModel(1L, 20L, "cat", "img.jpg", "title", "desc", "author", "2025-01-01T00:00:00", "ACTIVE", 0);
    }

    @Test
    void crearPublicacion_success_savesAndReturns() {
        when(usuarioClient.getUsuarioById(20L)).thenReturn(Map.of("id", 20L));
        when(publicationRepository.save(any(PublicationModel.class))).thenAnswer(i -> i.getArgument(0));

        PublicationModel result = publicationService.crearPublicacion(sample);

        assertThat(result.getUserid()).isEqualTo(20L);
        verify(publicationRepository, times(1)).save(any(PublicationModel.class));
    }

    @Test
    void crearPublicacion_usuarioNotFound_throws() {
        when(usuarioClient.getUsuarioById(20L)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> publicationService.crearPublicacion(sample));
        verify(publicationRepository, never()).save(any());
    }

    @Test
    void obtenerTodasPubli_returnsAll() {
        when(publicationRepository.findAll()).thenReturn(asList(sample));

        List<PublicationModel> list = publicationService.obtenerTodasPubli();

        assertThat(list).hasSize(1);
    }

    @Test
    void obtenerPubliPorUsuario_returnsList() {
        when(publicationRepository.findByUserid(20L)).thenReturn(asList(sample));

        List<PublicationModel> list = publicationService.obtenerPubliPorUsuario(20L);

        assertThat(list).hasSize(1);
    }

    @Test
    void eliminarPubli_exists_deletes() {
        when(publicationRepository.findById(1L)).thenReturn(Optional.of(sample));

        publicationService.eliminarPubli(1L);

        verify(publicationRepository).deleteById(1L);
    }

    @Test
    void eliminarPubli_notFound_throws() {
        when(publicationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> publicationService.eliminarPubli(99L));
        verify(publicationRepository, never()).deleteById(any());
    }

    @Test
    void buscarPublicacionPorId_found_returns() {
        when(publicationRepository.findById(1L)).thenReturn(Optional.of(sample));

        PublicationModel p = publicationService.buscarPublicacionPorId(1L);

        assertThat(p).isEqualTo(sample);
    }

    @Test
    void buscarPublicacionPorId_notFound_throws() {
        when(publicationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> publicationService.buscarPublicacionPorId(99L));
    }
}
