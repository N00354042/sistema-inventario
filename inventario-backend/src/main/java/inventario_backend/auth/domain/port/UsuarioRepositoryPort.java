package inventario_backend.auth.domain.port;

import java.util.Optional;
import inventario_backend.auth.domain.model.Usuario;

public interface UsuarioRepositoryPort {
    Optional<Usuario> findByUsername(String username);
    
    Usuario save(Usuario usuario); 
}