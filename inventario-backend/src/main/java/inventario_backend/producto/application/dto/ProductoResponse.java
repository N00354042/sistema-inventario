package inventario_backend.producto.application.dto;

public record ProductoResponse(
        Long id,
        String nombre,
        String descripcion,
        Double precio,
        Integer stock,
        Integer stockMinimo
) {
}