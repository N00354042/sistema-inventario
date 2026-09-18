package inventario_backend.movimiento.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import inventario_backend.movimiento.application.dto.MovimientoRequest;
import inventario_backend.movimiento.application.dto.MovimientoResponse;
import inventario_backend.movimiento.domain.model.Movimiento;
import inventario_backend.movimiento.domain.port.MovimientoRepositoryPort;
import inventario_backend.producto.domain.model.Producto;
import inventario_backend.producto.domain.port.ProductoRepositoryPort;

@ExtendWith(MockitoExtension.class)
class MovimientoServiceTest {

    @Mock
    private MovimientoRepositoryPort movimientoRepositoryPort;

    @Mock
    private ProductoRepositoryPort productoRepositoryPort;

    @InjectMocks
    private MovimientoService movimientoService;

    private Movimiento movimientoMock;
    private Producto productoMock;

    @BeforeEach
    void setUp() {
        productoMock = new Producto();
        productoMock.setId(1L);
        productoMock.setNombre("Mouse Gamer");
        productoMock.setStock(15);

        movimientoMock = new Movimiento(productoMock, "ENTRADA", 5, LocalDateTime.now());
        movimientoMock.setId(100L);
    }

    @Test
    void listarMovimientos_DebeRetornarListaConDatos() {
        when(movimientoRepositoryPort.obtenerTodosOrdenadosPorFecha())
                .thenReturn(Arrays.asList(movimientoMock));

        List<MovimientoResponse> resultado = movimientoService.listarMovimientos();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(movimientoRepositoryPort, times(1)).obtenerTodosOrdenadosPorFecha();
    }

    @Test
    void listarMovimientos_DebeRetornarListaVacia() {
        when(movimientoRepositoryPort.obtenerTodosOrdenadosPorFecha())
                .thenReturn(Collections.emptyList());

        List<MovimientoResponse> resultado = movimientoService.listarMovimientos();

        assertTrue(resultado.isEmpty());
        verify(movimientoRepositoryPort, times(1)).obtenerTodosOrdenadosPorFecha();
    }

    @Test
    void CP001_RegistroEntradaExitosa_DebeIncrementarStockA20() {
        MovimientoRequest request = new MovimientoRequest(1L, "ENTRADA", 5);
        when(productoRepositoryPort.findById(1L)).thenReturn(Optional.of(productoMock));
        when(movimientoRepositoryPort.guardar(any(Movimiento.class))).thenReturn(movimientoMock);

        MovimientoResponse response = movimientoService.registrarMovimiento(request);

        assertNotNull(response);
        assertEquals(20, productoMock.getStock());
        verify(productoRepositoryPort, times(1)).save(productoMock);
        verify(movimientoRepositoryPort, times(1)).guardar(any(Movimiento.class));
    }

    @Test
    void CP002_BloqueoPorCantidadNegativa_DebeLanzarExcepcion() {
        MovimientoRequest request = new MovimientoRequest(1L, "ENTRADA", -2);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            movimientoService.registrarMovimiento(request);
        });

        assertEquals("La cantidad debe ser mayor a cero", exception.getMessage());
        verify(productoRepositoryPort, never()).save(any(Producto.class));
    }

    @Test
    void CP003_RegistroSalidaExitosa_DebeReducirStockA3() {
        productoMock.setNombre("Teclado Gamer");
        productoMock.setStock(5);

        MovimientoRequest request = new MovimientoRequest(1L, "SALIDA", 2);
        when(productoRepositoryPort.findById(1L)).thenReturn(Optional.of(productoMock));
        when(movimientoRepositoryPort.guardar(any(Movimiento.class))).thenReturn(movimientoMock);

        MovimientoResponse response = movimientoService.registrarMovimiento(request);

        assertNotNull(response);
        assertEquals(3, productoMock.getStock());
        verify(productoRepositoryPort, times(1)).save(productoMock);
    }
}