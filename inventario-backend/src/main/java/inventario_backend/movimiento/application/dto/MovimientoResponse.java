package inventario_backend.movimiento.application.dto;

import java.time.LocalDateTime;

public record MovimientoResponse(
        Long id,
        String producto,
        String tipo,
        int cantidad,
        LocalDateTime fecha) {
}