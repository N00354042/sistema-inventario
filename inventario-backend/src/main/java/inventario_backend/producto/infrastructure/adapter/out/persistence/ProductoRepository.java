package inventario_backend.producto.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import inventario_backend.producto.domain.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
}