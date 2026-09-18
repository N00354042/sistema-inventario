package inventario_backend.producto.application.mapper;

import inventario_backend.producto.application.dto.ProductoRequest;
import inventario_backend.producto.application.dto.ProductoResponse;
import inventario_backend.producto.domain.model.Producto;

public class ProductoMapper {

    private ProductoMapper() {
    }

    public static ProductoResponse toResponse(Producto producto) {
        if (producto == null) {
            return null;
        }
        return new ProductoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getStockMinimo()
        );
    }

    public static Producto toEntity(ProductoRequest request) {
        if (request == null) {
            return null;
        }
        Producto producto = new Producto();
        producto.setNombre(request.nombre());
        producto.setDescripcion(request.descripcion());
        producto.setPrecio(request.precio());
        producto.setStock(request.stock());
        producto.setStockMinimo(request.stockMinimo());
        return producto;
    }

    public static void updateEntity(Producto existente, ProductoRequest request) {
        if (request != null && existente != null) {
            existente.setNombre(request.nombre());
            existente.setDescripcion(request.descripcion());
            existente.setPrecio(request.precio());
            existente.setStock(request.stock());
            existente.setStockMinimo(request.stockMinimo());
        }
    }
}