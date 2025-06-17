package org.gamesstore.gamesstoreapi;


import org.gamesstore.gamesstoreapi.order.model.Pedido;
import org.gamesstore.gamesstoreapi.order.repository.PedidoRepository;
import org.gamesstore.gamesstoreapi.order.service.PedidoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoServiceTest {

    private PedidoRepository pedidoRepository;
    private PedidoService pedidoService;

    @BeforeEach
    void setUp() {
        pedidoRepository = mock(PedidoRepository.class);
        pedidoService = new PedidoService(pedidoRepository);
    }

    private Pedido createPedido(Long id) {
        Pedido pedido = new Pedido();
        pedido.setId(id);
        return pedido;
    }

    @Test
    void shouldListAllPedidos() {
        List<Pedido> pedidos = Arrays.asList(createPedido(1L), createPedido(2L));
        when(pedidoRepository.findAll()).thenReturn(pedidos);

        List<Pedido> result = pedidoService.listarTodos();

        assertEquals(2, result.size());
        verify(pedidoRepository).findAll();
    }

    @Test
    void shouldFindPedidoById() {
        Pedido pedido = createPedido(1L);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        Optional<Pedido> result = pedidoService.buscarPorId(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(pedidoRepository).findById(1L);
    }

    @Test
    void shouldReturnEmptyWhenPedidoNotFound() {
        when(pedidoRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Pedido> result = pedidoService.buscarPorId(99L);

        assertFalse(result.isPresent());
        verify(pedidoRepository).findById(99L);
    }

    @Test
    void shouldSavePedido() {
        Pedido pedido = createPedido(null);
        Pedido savedPedido = createPedido(1L);

        when(pedidoRepository.save(pedido)).thenReturn(savedPedido);

        Pedido result = pedidoService.salvar(pedido);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(pedidoRepository).save(pedido);
    }

    @Test
    void shouldDeletePedidoById() {
        doNothing().when(pedidoRepository).deleteById(1L);

        pedidoService.deletar(1L);

        verify(pedidoRepository).deleteById(1L);
    }
}
