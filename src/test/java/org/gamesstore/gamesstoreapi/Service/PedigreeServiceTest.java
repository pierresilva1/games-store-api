package org.gamesstore.gamesstoreapi.Service;


import org.gamesstore.gamesstoreapi.order.model.Pedido;
import org.gamesstore.gamesstoreapi.order.repository.PedigreeRepository;
import org.gamesstore.gamesstoreapi.order.service.PedigreeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedigreeServiceTest {

    private PedigreeRepository pedigreeRepository;
    private PedigreeService pedigreeService;

    @BeforeEach
    void setUp() {
        pedigreeRepository = mock(PedigreeRepository.class);
        pedigreeService = new PedigreeService(pedigreeRepository);
    }

    private Pedido createPedido(Long id) {
        Pedido pedido = new Pedido();
        pedido.setId(id);
        return pedido;
    }

    @Test
    void shouldListAllPedidos() {
        List<Pedido> pedidos = Arrays.asList(createPedido(1L), createPedido(2L));
        when(pedigreeRepository.findAll()).thenReturn(pedidos);

        List<Pedido> result = pedigreeService.listarTodos();

        assertEquals(2, result.size());
        verify(pedigreeRepository).findAll();
    }

    @Test
    void shouldFindPedidoById() {
        Pedido pedido = createPedido(1L);
        when(pedigreeRepository.findById(1L)).thenReturn(Optional.of(pedido));

        Optional<Pedido> result = pedigreeService.buscarPorId(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(pedigreeRepository).findById(1L);
    }

    @Test
    void shouldReturnEmptyWhenPedidoNotFound() {
        when(pedigreeRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Pedido> result = pedigreeService.buscarPorId(99L);

        assertFalse(result.isPresent());
        verify(pedigreeRepository).findById(99L);
    }

    @Test
    void shouldSavePedido() {
        Pedido pedido = createPedido(null);
        Pedido savedPedido = createPedido(1L);

        when(pedigreeRepository.save(pedido)).thenReturn(savedPedido);

        Pedido result = pedigreeService.salvar(pedido);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(pedigreeRepository).save(pedido);
    }

    @Test
    void shouldDeletePedidoById() {
        doNothing().when(pedigreeRepository).deleteById(1L);

        pedigreeService.deletar(1L);

        verify(pedigreeRepository).deleteById(1L);
    }
}
