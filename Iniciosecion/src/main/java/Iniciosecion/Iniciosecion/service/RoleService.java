package Iniciosecion.Iniciosecion.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Iniciosecion.Iniciosecion.model.Rol;
import Iniciosecion.Iniciosecion.repository.RoleRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional

public class RoleService {

    @Autowired 
    private RoleRepository roleRepository;

    public List<Rol> buscarRol(){
        return roleRepository.findAll();
    }

    public Rol buscarRol(Long id ){
        return roleRepository.findById(id).orElseThrow(()-> new RuntimeException("Rol no encontrado"));
    }


}
