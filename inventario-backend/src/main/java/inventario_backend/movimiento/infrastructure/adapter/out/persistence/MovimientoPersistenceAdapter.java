package inventario_backend.movimiento.infrastructure.adapter.out.persistence;

import java.util.List;
import org.springframework.stereotype.Component;

import inventario_backend.movimiento.domain.model.Movimiento;
import inventario_backend.movimiento.domain.port.MovimientoRepositoryPort;

@Component
public class MovimientoPersistenceAdapter implements MovimientoRepositoryPort {

    private final MovimientoRepository movimientoRepository;

    public MovimientoPersistenceAdapter(MovimientoRepository movimientoRepository) {
        this.movimientoRepository = movimientoRepository;
    }

    @Override
    public List<Movimiento> obtenerTodosOrdenadosPorFecha() {
        return movimientoRepository.findAllByOrderByFechaDesc();
    }

    @Override
    public Movimiento guardar(Movimiento movimiento) {
        return movimientoRepository.save(movimiento);
    }
}