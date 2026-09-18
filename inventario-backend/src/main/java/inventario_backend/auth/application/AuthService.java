package inventario_backend.auth.application;

import org.springframework.stereotype.Service;

import inventario_backend.auth.application.dto.LoginRequest;
import inventario_backend.auth.application.dto.LoginResponse;
import inventario_backend.auth.domain.model.Usuario;
import inventario_backend.auth.domain.port.UsuarioRepositoryPort;
import inventario_backend.shared.security.JwtUtil;

@Service
public class AuthService {

    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public AuthService(UsuarioRepositoryPort usuarioRepositoryPort) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepositoryPort.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!usuario.getPassword().equals(request.password())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        String token = JwtUtil.generateToken(
                usuario.getUsername(),
                usuario.getRol()
        );
        
        return new LoginResponse(token);
    }
}