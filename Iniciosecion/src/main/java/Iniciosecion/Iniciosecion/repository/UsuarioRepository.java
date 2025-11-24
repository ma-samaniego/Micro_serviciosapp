package Iniciosecion.Iniciosecion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Iniciosecion.Iniciosecion.model.Usuario;

@Repository

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {


}
