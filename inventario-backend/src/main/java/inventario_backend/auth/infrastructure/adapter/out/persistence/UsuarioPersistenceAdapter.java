package inventario_backend.auth.infrastructure.adapter.out.persistence;

import java.util.Optional;
import org.springframework.stereotype.Component;

import inventario_backend.auth.domain.model.Usuario;
import inventario_backend.auth.domain.port.UsuarioRepositoryPort;

@Component
public class UsuarioPersistenceAdapter implements UsuarioRepositoryPort {

    private final UsuarioRepository usuarioRepository;

    public UsuarioPersistenceAdapter(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Optional<Usuario> findByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    @Override
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
}