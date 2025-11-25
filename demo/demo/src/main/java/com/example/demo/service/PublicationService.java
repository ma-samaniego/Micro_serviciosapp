package com.example.demo.service;

import java.util.List;
import java.util.Map;
import java.time.LocalDateTime; // Import necesario para la fecha

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.PublicationModel;
import com.example.demo.repository.PublicationRepository;
import com.example.demo.webClient.UsuarioClient;

@Service
public class PublicationService {
    @Autowired
    private PublicationRepository publicationRepository;

    @Autowired
    private UsuarioClient usuarioClient;

    public PublicationModel crearPublicacion(PublicationModel crearPubli) {
        // 1. Validar que el usuario exista
        Map<String, Object> usuarioData = usuarioClient.getUsuarioById(crearPubli.getUserid());
        if (usuarioData == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        // 2. Asignar fecha automáticamente si no viene (CORRECCIÓN)
        if (crearPubli.getCreateDt() == null || crearPubli.getCreateDt().isBlank()) {
            crearPubli.setCreateDt(LocalDateTime.now().toString());
        }

        // 3. Asignar estado por defecto si no viene
        if (crearPubli.getStatus() == null) {
            crearPubli.setStatus("ACTIVE");
        }

        return publicationRepository.save(crearPubli);
    }

    public List<PublicationModel> obtenerTodasPubli() {
        return publicationRepository.findAll();
    }

    public List<PublicationModel> obtenerPubliPorUsuario(Long userid) {
        return publicationRepository.findByUserid(userid);
    }

    public void eliminarPubli(Long publicationId) {
        // 1. Buscar si la publicación existe
        PublicationModel publi = publicationRepository.findById(publicationId)
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));

        // 2. Eliminar publicación directamente
        publicationRepository.deleteById(publicationId);
    }

    public PublicationModel buscarPublicacionPorId(Long publicationId) {
        return publicationRepository.findById(publicationId)
                .orElseThrow(() -> new RuntimeException("No encontrado"));
    }
}