package inventario_backend.movimiento.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import inventario_backend.movimiento.application.dto.MovimientoRequest;
import inventario_backend.movimiento.application.dto.MovimientoResponse;
import inventario_backend.movimiento.application.mapper.MovimientoMapper;
import inventario_backend.movimiento.domain.model.Movimiento;
import inventario_backend.movimiento.domain.port.MovimientoRepositoryPort;
import inventario_backend.producto.domain.model.Producto;
import inventario_backend.producto.domain.port.ProductoRepositoryPort;

@Service
public class MovimientoService {

    private final MovimientoRepositoryPort movimientoRepositoryPort;
    private final ProductoRepositoryPort productoRepositoryPort;

    public MovimientoService(MovimientoRepositoryPort movimientoRepositoryPort,
            ProductoRepositoryPort productoRepositoryPort) {
        this.movimientoRepositoryPort = movimientoRepositoryPort;
        this.productoRepositoryPort = productoRepositoryPort;
    }

    public List<MovimientoResponse> listarMovimientos() {
        List<Movimiento> movimientos = movimientoRepositoryPort.obtenerTodosOrdenadosPorFecha();

        return movimientos.stream()
                .map(MovimientoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public MovimientoResponse registrarMovimiento(MovimientoRequest request) {
        if (request.cantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }

        Producto producto = productoRepositoryPort.findById(request.productoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (request.tipo().equalsIgnoreCase("ENTRADA")) {
            producto.setStock(producto.getStock() + request.cantidad());
        } else if (request.tipo().equalsIgnoreCase("SALIDA")) {
            if (producto.getStock() < request.cantidad()) {
                throw new IllegalArgumentException("Stock insuficiente para realizar la operación");
            }
            producto.setStock(producto.getStock() - request.cantidad());
        } else {
            throw new IllegalArgumentException("Tipo de movimiento inválido");
        }

        productoRepositoryPort.save(producto);

        Movimiento movimiento = MovimientoMapper.toEntity(request);
        movimiento.setProducto(producto);
        Movimiento guardado = movimientoRepositoryPort.guardar(movimiento);

        return MovimientoMapper.toResponse(guardado);
    }
}