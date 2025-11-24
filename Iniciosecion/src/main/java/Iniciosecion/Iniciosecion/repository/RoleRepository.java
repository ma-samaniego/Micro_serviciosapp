package Iniciosecion.Iniciosecion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Iniciosecion.Iniciosecion.model.Rol;

@Repository
public interface RoleRepository extends JpaRepository<Rol, Long> {

}
