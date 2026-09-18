package inventario_backend.producto.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import inventario_backend.movimiento.domain.model.Movimiento;
import inventario_backend.movimiento.domain.port.MovimientoRepositoryPort;
import inventario_backend.producto.application.dto.ProductoRequest;
import inventario_backend.producto.application.dto.ProductoResponse;
import inventario_backend.producto.application.mapper.ProductoMapper;
import inventario_backend.producto.domain.model.Producto;
import inventario_backend.producto.domain.port.ProductoRepositoryPort;
import inventario_backend.shared.exception.ResourceNotFoundException;

@Service
public class ProductoService {

    private final ProductoRepositoryPort productoRepositoryPort;
    private final MovimientoRepositoryPort movimientoRepositoryPort;

    public ProductoService(
            ProductoRepositoryPort productoRepositoryPort,
            MovimientoRepositoryPort movimientoRepositoryPort) {
        this.productoRepositoryPort = productoRepositoryPort;
        this.movimientoRepositoryPort = movimientoRepositoryPort;
    }

    @Transactional(readOnly = true)
    public List<ProductoResponse> listar() {
        return productoRepositoryPort.findAll().stream()
                .map(ProductoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductoResponse guardar(ProductoRequest request) {
        Producto nuevoProducto = ProductoMapper.toEntity(request);
        Producto productoGuardado = productoRepositoryPort.save(nuevoProducto);

        Movimiento movimiento = new Movimiento(
                productoGuardado,
                "ENTRADA",
                productoGuardado.getStock(),
                LocalDateTime.now());
        movimientoRepositoryPort.guardar(movimiento);

        return ProductoMapper.toResponse(productoGuardado);
    }

    @Transactional
    public ProductoResponse actualizar(Long id, ProductoRequest request) {
        Producto existente = productoRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));

        ProductoMapper.updateEntity(existente, request);
        Producto productoActualizado = productoRepositoryPort.save(existente);

        return ProductoMapper.toResponse(productoActualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        if (productoRepositoryPort.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Producto no encontrado con ID: " + id);
        }
        productoRepositoryPort.deleteById(id);
    }
}