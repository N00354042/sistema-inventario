package inventario_backend.movimiento.application.mapper;

import java.time.LocalDateTime;

import inventario_backend.movimiento.application.dto.MovimientoRequest;
import inventario_backend.movimiento.application.dto.MovimientoResponse;
import inventario_backend.movimiento.domain.model.Movimiento;
import inventario_backend.producto.domain.model.Producto; // Import necesario

public class MovimientoMapper {

    private MovimientoMapper() {
    }

    public static MovimientoResponse toResponse(Movimiento movimiento) {
        if (movimiento == null) {
            return null;
        }

        return new MovimientoResponse(
                movimiento.getId(),
                movimiento.getProducto() != null ? movimiento.getProducto().getNombre() : "Desconocido",
                movimiento.getTipo(),
                movimiento.getCantidad(),
                movimiento.getFecha());
    }

    public static Movimiento toEntity(MovimientoRequest request) {
        if (request == null) {
            return null;
        }

        Movimiento movimiento = new Movimiento();

        // Creamos una instancia de Producto solo con su ID para guardar la llave
        // foránea
        Producto productoRef = new Producto();
        productoRef.setId(request.productoId());

        movimiento.setProducto(productoRef);
        movimiento.setTipo(request.tipo());
        movimiento.setCantidad(request.cantidad());
        movimiento.setFecha(LocalDateTime.now());

        return movimiento;
    }
}