package com.example.demo.service;

import java.util.List;
import java.util.Map;

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
        Map<String, Object> usuarioData = usuarioClient.getUsuarioById(crearPubli.getUserid());
        if (usuarioData == null) {
            throw new RuntimeException("Usuario no encontrado");
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

        // 🟢 CAMBIO: Borramos toda la lógica que verificaba al usuario dueño.
        // Simplemente la borramos, confiando en que si llegamos aquí es porque
        // el Admin dio la orden desde la App.
        
        // 2. Eliminar publicación directamente
        publicationRepository.deleteById(publicationId);
    }

    public PublicationModel buscarPublicacionPorId(Long publicationId) {
        return publicationRepository.findById(publicationId).orElseThrow(() -> new RuntimeException("No encontrado"));

    }

}
