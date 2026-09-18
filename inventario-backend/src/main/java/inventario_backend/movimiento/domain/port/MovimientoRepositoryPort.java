package inventario_backend.movimiento.domain.port;

import java.util.List;
import inventario_backend.movimiento.domain.model.Movimiento;

public interface MovimientoRepositoryPort {
    List<Movimiento> obtenerTodosOrdenadosPorFecha();
    Movimiento guardar(Movimiento movimiento);
}