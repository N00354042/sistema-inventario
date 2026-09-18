package inventario_backend.movimiento.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import inventario_backend.movimiento.application.dto.MovimientoResponse;
import inventario_backend.movimiento.application.mapper.MovimientoMapper;
import inventario_backend.movimiento.domain.model.Movimiento;
import inventario_backend.movimiento.domain.port.MovimientoRepositoryPort;

@Service
public class MovimientoService {

    private final MovimientoRepositoryPort movimientoRepositoryPort;

    public MovimientoService(MovimientoRepositoryPort movimientoRepositoryPort) {
        this.movimientoRepositoryPort = movimientoRepositoryPort;
    }

    public List<MovimientoResponse> listarMovimientos() {
        List<Movimiento> movimientos = movimientoRepositoryPort.obtenerTodosOrdenadosPorFecha();

        return movimientos.stream()
                .map(MovimientoMapper::toResponse)
                .collect(Collectors.toList());
    }
}