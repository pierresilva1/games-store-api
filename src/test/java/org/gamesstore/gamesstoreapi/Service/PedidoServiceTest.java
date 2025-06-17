package org.gamesstore.gamesstoreapi.service;

import org.gamesstore.gamesstoreapi.client.model.Client;
import org.gamesstore.gamesstoreapi.client.repository.ClientRepository;
import org.gamesstore.gamesstoreapi.order.dto.PedidoRequestDTO;
import org.gamesstore.gamesstoreapi.order.model.OrderStatus;
import org.gamesstore.gamesstoreapi.order.model.Pedido;
import org.gamesstore.gamesstoreapi.order.repository.PedidoRepository;
import org.gamesstore.gamesstoreapi.order.service.PedidoService;
import org.gamesstore.gamesstoreapi.product.model.Product;
import org.gamesstore.gamesstoreapi.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoServiceTest {

    private PedidoRepository pedidoRepository;
    private ProductRepository productRepository;
    private ClientRepository clientRepository;
    private PedidoService pedidoService;

    @BeforeEach
    void setUp() {
        pedidoRepository = mock(PedidoRepository.class);
        productRepository = mock(ProductRepository.class);
        clientRepository = mock(ClientRepository.class);
        pedidoService = new PedidoService(pedidoRepository, productRepository, clientRepository);
    }

    @Test
    void shouldListAllPedidos() {
        List<Pedido> pedidos = Arrays.asList(new Pedido(), new Pedido());
        when(pedidoRepository.findAll()).thenReturn(pedidos);

        List<Pedido> result = pedidoService.listarTodos();

        assertEquals(2, result.size());
        verify(pedidoRepository).findAll();
    }

    @Test
    void shouldFindPedidoById() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);
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
    void shouldSavePedidoFromRequestDTO() {
        // Preparando mocks
        Client client = new Client();
        client.setId(1L);

        Product product = new Product();
        product.setId(10L);

        Pedido savedPedido = new Pedido();
        savedPedido.setId(100L);
        savedPedido.setCliente(client);
        savedPedido.setProdutos(List.of(product));
        savedPedido.setStatus(OrderStatus.NOVO);
       

        PedidoRequestDTO dto = new PedidoRequestDTO();
        dto.setClienteId(1L);
        dto.setProdutoIds(List.of(10L));

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(productRepository.findAllById(dto.getProdutoIds())).thenReturn(List.of(product));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(savedPedido);

        Pedido result = pedidoService.salvar(dto);

        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals(OrderStatus.NOVO, result.getStatus());
        verify(clientRepository).findById(1L);
        verify(productRepository).findAllById(dto.getProdutoIds());
        verify(pedidoRepository).save(any(Pedido.class));
    }

    @Test
    void shouldThrowExceptionWhenClienteNotFound() {
        PedidoRequestDTO dto = new PedidoRequestDTO();
        dto.setClienteId(1L);
        dto.setProdutoIds(List.of(10L));

        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> pedidoService.salvar(dto));
        assertEquals("Cliente não encontrado", exception.getMessage());
    }

    @Test
    void shouldDeletePedidoById() {
        when(pedidoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(pedidoRepository).deleteById(1L);

        pedidoService.deletar(1L);

        verify(pedidoRepository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenPedidoToDeleteNotFound() {
        when(pedidoRepository.existsById(999L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> pedidoService.deletar(999L));
        assertEquals("Pedido não encontrado", exception.getMessage());
    }
}
