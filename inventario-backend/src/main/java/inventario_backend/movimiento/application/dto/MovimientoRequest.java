package inventario_backend.movimiento.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MovimientoRequest(
                @NotNull(message = "El identificador del producto es obligatorio") Long productoId,

                @NotBlank(message = "El tipo de movimiento es obligatorio") String tipo,

                @NotNull(message = "La cantidad es obligatoria") @Min(value = 1, message = "La cantidad debe ser mayor a cero") Integer cantidad) {
}