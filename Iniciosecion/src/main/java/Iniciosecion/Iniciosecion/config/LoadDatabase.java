package Iniciosecion.Iniciosecion.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import Iniciosecion.Iniciosecion.model.EstadoUsuario;
import Iniciosecion.Iniciosecion.model.Rol;
import Iniciosecion.Iniciosecion.model.Usuario;
import Iniciosecion.Iniciosecion.repository.RoleRepository;
import Iniciosecion.Iniciosecion.repository.UsuarioRepository;

@Configuration

public class LoadDatabase {

    @Bean
    CommandLineRunner iniDatabase(RoleRepository roleRepo, UsuarioRepository usuarioRepo) {
        return args -> {
            if (roleRepo.count() == 0 && usuarioRepo.count() == 0) {
                Rol admin = new Rol();
                admin.setNombre_rol("ADMIN");
                admin.setPermisos("INFORMES,ELIMINAR CUENTAS,DAR ROLES");
                roleRepo.save(admin);

                Rol user = new Rol();
                user.setNombre_rol("USER");
                user.setPermisos("LEER,PUBLICAR,COMENTAR");
                roleRepo.save(user);

                usuarioRepo.save(new Usuario(null, "cadex", "123", "1234567890", "cadex@example.com",
                        EstadoUsuario.ACTIVO, admin));
            } else {
                System.out.println("Los datos ya existen, no se cargan datos iniciales");
            }
        };

    }
}
