package com.libros.reserva_libros.Service;

import com.libros.reserva_libros.model.Ejemplar;
import com.libros.reserva_libros.model.EstadoEjemplar;
import com.libros.reserva_libros.model.Libro;
import com.libros.reserva_libros.repository.EjemplarRepository;
import com.libros.reserva_libros.service.EjemplarService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class EjemplarServiceTest {

    @Mock
    private EjemplarRepository ejemplarRepository;

    @InjectMocks
    private EjemplarService ejemplarService;

    private Ejemplar ejemplar;

    @BeforeEach
    void setUp() {
        ejemplar = new Ejemplar();
        ejemplar.setId(1L);
        ejemplar.setCodigoEjemplar("COD123");
        ejemplar.setEstado(EstadoEjemplar.DISPONIBLE);
        ejemplar.setUbicacion("Estante A");
    }

    @Test
    void testListarEjemplares() {
        when(ejemplarRepository.findAll()).thenReturn(List.of(ejemplar));

        List<Ejemplar> resultado = ejemplarService.listarEjemplares();

        assertEquals(1, resultado.size());
        verify(ejemplarRepository, times(1)).findAll();
    }

    @Test
    void testGuardarEjemplar() {
        when(ejemplarRepository.save(ejemplar)).thenReturn(ejemplar);

        Ejemplar guardado = ejemplarService.guardarEjemplar(ejemplar);

        assertNotNull(guardado);
        assertEquals("COD123", guardado.getCodigoEjemplar());
    }

    @Test
    void testBuscarEjemplarPorId() {
        when(ejemplarRepository.findById(1L)).thenReturn(Optional.of(ejemplar));

        Optional<Ejemplar> encontrado = ejemplarService.buscarEjemplarporId(1L);

        assertTrue(encontrado.isPresent());
        assertEquals("COD123", encontrado.get().getCodigoEjemplar());
    }

    @Test
    void testActualizarEjemplar() {
        Ejemplar actualizado = new Ejemplar();
        actualizado.setId(1L);
        actualizado.setCodigoEjemplar("COD456");
        actualizado.setEstado(EstadoEjemplar.RESERVADO);
        actualizado.setUbicacion("Estante B");

        when(ejemplarRepository.findById(1L)).thenReturn(Optional.of(ejemplar));
        when(ejemplarRepository.save(any(Ejemplar.class))).thenReturn(actualizado);

        ejemplarService.actualizarEjemplar(actualizado);

        assertEquals("COD456", ejemplar.getCodigoEjemplar());
        assertEquals(EstadoEjemplar.RESERVADO, ejemplar.getEstado());
        assertEquals("Estante B", ejemplar.getUbicacion());
    }

    @Test
    void testEliminarEjemplar() {
        ejemplarService.eliminarEjemplar(1L);
        verify(ejemplarRepository).deleteById(1L);
    }

    @Test
    void testGetEstados() {
        List<EstadoEjemplar> estados = ejemplarService.getEstados();
        assertTrue(estados.contains(EstadoEjemplar.DISPONIBLE));
        assertTrue(estados.contains(EstadoEjemplar.RESERVADO));
        assertTrue(estados.contains(EstadoEjemplar.PRESTADO));
    }

    @Test
    void testListarEjemplaresDisponibles() {
        when(ejemplarRepository.findByEstado(EstadoEjemplar.DISPONIBLE)).thenReturn(List.of(ejemplar));

        List<Ejemplar> disponibles = ejemplarService.listarEjemplaresDisponibles(List.of(ejemplar));

        assertEquals(1, disponibles.size());
        assertEquals(EstadoEjemplar.DISPONIBLE, disponibles.get(0).getEstado());
    }

    @Test
    void testListarEjemplaresDisponiblesPorLibro() {
        Libro libro = new Libro();
        when(ejemplarRepository.findByLibroAndEstado(libro, EstadoEjemplar.DISPONIBLE)).thenReturn(List.of(ejemplar));

        List<Ejemplar> disponibles = ejemplarService.listarEjemplaresDisponibles(libro);

        assertEquals(1, disponibles.size());
    }

    @Test
    void testHayEjemplarDisponiblePorLibro() {
        Libro libro = new Libro();
        when(ejemplarRepository.existsByLibroAndEstado(libro, EstadoEjemplar.DISPONIBLE)).thenReturn(true);

        boolean hayDisponible = ejemplarService.hayEjemplarDisponiblePorLibro(libro);

        assertTrue(hayDisponible);
    }
}
