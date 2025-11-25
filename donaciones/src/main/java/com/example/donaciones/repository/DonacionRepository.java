package com.example.donaciones.repository;

import com.example.donaciones.model.DonacionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DonacionRepository extends JpaRepository<DonacionModel, Long> {
    // Ver donaciones hechas por un usuario
    List<DonacionModel> findByUsuarioDonanteId(Long usuarioDonanteId);
}