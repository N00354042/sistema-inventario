package inventario_backend.producto.domain.port;

import java.util.List;
import java.util.Optional;

import inventario_backend.producto.domain.model.Producto;

public interface ProductoRepositoryPort {
    List<Producto> findAll();
    Optional<Producto> findById(Long id);
    Producto save(Producto producto);
    void deleteById(Long id);
}