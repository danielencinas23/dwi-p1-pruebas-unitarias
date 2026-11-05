package com.dwi.p1.service;

import com.dwi.p1.model.Producto;
import com.dwi.p1.repository.ProductoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    @Test
    @DisplayName("findAll devuelve los productos que entrega el repositorio")
    void findAllDevuelveProductos() {
        Producto teclado = productoConId(1L, "Teclado", "450.00");
        Producto mouse = productoConId(2L, "Mouse", "250.50");
        when(productoRepository.findAll()).thenReturn(List.of(teclado, mouse));

        List<Producto> resultado = productoService.findAll();

        assertThat(resultado).containsExactly(teclado, mouse);
        verify(productoRepository).findAll();
        verifyNoMoreInteractions(productoRepository);
    }

    @Test
    @DisplayName("findAll devuelve una lista vacia si no hay productos")
    void findAllSinProductosDevuelveListaVacia() {
        when(productoRepository.findAll()).thenReturn(List.of());

        List<Producto> resultado = productoService.findAll();

        assertThat(resultado).isEmpty();
        verify(productoRepository).findAll();
    }

    @Test
    @DisplayName("create persiste el producto y devuelve sus atributos con el id generado")
    void createPersisteProducto() {
        Producto nuevo = new Producto("Monitor", new BigDecimal("3200.00"));
        when(productoRepository.save(any(Producto.class)))
                .thenReturn(productoConId(10L, "Monitor", "3200.00"));

        Producto resultado = productoService.create(nuevo);

        assertThat(resultado.getId()).isEqualTo(10L);
        assertThat(resultado.getNombre()).isEqualTo("Monitor");
        assertThat(resultado.getPrecio()).isEqualByComparingTo("3200.00");
        verify(productoRepository).save(nuevo);
        verifyNoMoreInteractions(productoRepository);
    }

    @Test
    @DisplayName("create descarta el id recibido para no sobrescribir un registro existente")
    void createDescartaIdRecibido() {
        Producto conIdExistente = productoConId(99L, "Audifonos", "800.00");
        when(productoRepository.save(any(Producto.class))).thenAnswer(llamada -> llamada.getArgument(0));

        productoService.create(conIdExistente);

        ArgumentCaptor<Producto> guardado = ArgumentCaptor.forClass(Producto.class);
        verify(productoRepository).save(guardado.capture());
        assertThat(guardado.getValue().getId()).isNull();
        assertThat(guardado.getValue().getNombre()).isEqualTo("Audifonos");
        assertThat(guardado.getValue().getPrecio()).isEqualByComparingTo("800.00");
    }

    private Producto productoConId(Long id, String nombre, String precio) {
        Producto producto = new Producto(nombre, new BigDecimal(precio));
        producto.setId(id);
        return producto;
    }
}
