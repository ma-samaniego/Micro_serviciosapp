package com.example.donaciones.service;

import com.example.donaciones.model.DonacionModel;
import com.example.donaciones.repository.DonacionRepository;
import com.example.donaciones.webClient.UsuarioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class DonacionService {

    @Autowired
    private DonacionRepository donacionRepository;

    @Autowired
    private UsuarioClient usuarioClient;

    // PublicacionClient ELIMINADO

    public DonacionModel crearDonacion(DonacionModel donacion) {
        // 1. Validar que el usuario donante existe
        Map<String, Object> usuario = usuarioClient.getUsuarioById(donacion.getUsuarioDonanteId());
        if (usuario == null) {
            throw new RuntimeException("El usuario donante no existe");
        }

        // SE ELIMINÓ la validación de Publicación

        // 2. Validar monto positivo
        if (donacion.getMonto() <= 0) {
            throw new RuntimeException("El monto debe ser mayor a 0");
        }

        return donacionRepository.save(donacion);
    }

    public List<DonacionModel> obtenerPorUsuario(Long usuarioId) {
        return donacionRepository.findByUsuarioDonanteId(usuarioId);
    }
}