package com.example.comentarios.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.comentarios.model.ComentarioModel;

@Repository
public interface ComentarioRepository extends JpaRepository<ComentarioModel, Long> { // <-- Nombre Corregido
    List<ComentarioModel> findByPublicationId(Long publicationId);
    List<ComentarioModel> findByUsuarioId(Long usuarioId);
}