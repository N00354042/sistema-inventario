package inventario_backend.producto.infrastructure.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

import inventario_backend.producto.domain.model.Producto;
import inventario_backend.producto.domain.port.ProductoRepositoryPort;

@Component
public class ProductoPersistenceAdapter implements ProductoRepositoryPort {

    private final ProductoRepository productoRepository;

    public ProductoPersistenceAdapter(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    @Override
    public Optional<Producto> findById(Long id) {
        return productoRepository.findById(id);
    }

    @Override
    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public void deleteById(Long id) {
        productoRepository.deleteById(id);
    }
}