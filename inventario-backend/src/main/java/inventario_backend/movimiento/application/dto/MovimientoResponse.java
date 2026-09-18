package inventario_backend.movimiento.application.dto;

import java.time.LocalDateTime;

public record MovimientoResponse(
                Long id,
                String producto, // Seguirá devolviendo el nombre para la UI
                String tipo,
                int cantidad,
                LocalDateTime fecha) {
}