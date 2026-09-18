package inventario_backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import inventario_backend.auth.domain.model.Usuario;
import inventario_backend.auth.domain.port.UsuarioRepositoryPort;

@Component
public class DataLoader implements CommandLineRunner {

    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public DataLoader(UsuarioRepositoryPort usuarioRepositoryPort) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepositoryPort.findByUsername("admin").isEmpty()) {
            Usuario usuario = new Usuario();
            usuario.setUsername("admin");
            usuario.setPassword("1234");
            usuario.setRol("ADMIN");
            usuarioRepositoryPort.save(usuario);

            Usuario empleado = new Usuario();
            empleado.setUsername("empleado");
            empleado.setPassword("1234");
            empleado.setRol("EMPLEADO");
            usuarioRepositoryPort.save(empleado);

            System.out.println("Usuarios creados exitosamente mediante el Puerto");
        }
    }
}