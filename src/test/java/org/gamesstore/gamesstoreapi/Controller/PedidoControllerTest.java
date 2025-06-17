package org.gamesstore.gamesstoreapi.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.gamesstore.gamesstoreapi.client.model.Client;
import org.gamesstore.gamesstoreapi.order.controller.PedidoController;
import org.gamesstore.gamesstoreapi.order.dto.PedidoRequestDTO;
import org.gamesstore.gamesstoreapi.order.dto.PedidoResponseDTO;
import org.gamesstore.gamesstoreapi.order.model.OrderStatus;
import org.gamesstore.gamesstoreapi.order.model.Pedido;
import org.gamesstore.gamesstoreapi.order.service.PedidoService;
import org.gamesstore.gamesstoreapi.product.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PedidoController.class)
public class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoService pedidoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Pedido samplePedido;
    private PedidoResponseDTO sampleResponseDTO;
    private PedidoRequestDTO sampleRequestDTO;
    private Client sampleClient;
    private Product sampleProduct1, sampleProduct2;

    @BeforeEach
    void setup() {
        sampleClient = new Client();
        sampleClient.setId(1L);
        sampleClient.setName("Cliente Teste");

        sampleProduct1 = new Product();
        sampleProduct1.setId(1L);
        sampleProduct1.setName("Produto 1");

        sampleProduct2 = new Product();
        sampleProduct2.setId(2L);
        sampleProduct2.setName("Produto 2");

        samplePedido = new Pedido();
        samplePedido.setId(1L);
        samplePedido.setCliente(sampleClient);
        samplePedido.setProdutos(List.of(sampleProduct1, sampleProduct2));
        samplePedido.setStatus(OrderStatus.NOVO);
        samplePedido.setDataCriacao(LocalDateTime.now());

        sampleResponseDTO = new PedidoResponseDTO(
                samplePedido.getId(),
                samplePedido.getCliente(),
                samplePedido.getProdutos(),
                samplePedido.getStatus(),
                samplePedido.getDataCriacao()
        );

        sampleRequestDTO = PedidoRequestDTO.builder()
                .clienteId(sampleClient.getId())
                .produtoIds(List.of(sampleProduct1.getId(), sampleProduct2.getId()))
                .build();
    }

    @Test
    void testListarTodos() throws Exception {
        Mockito.when(pedidoService.listarTodos()).thenReturn(List.of(samplePedido));
        Mockito.when(pedidoService.toResponse(samplePedido)).thenReturn(sampleResponseDTO);

        mockMvc.perform(get("/api/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(samplePedido.getId()))
                .andExpect(jsonPath("$[0].status").value(samplePedido.getStatus().toString()));
    }

    @Test
    void testBuscarPorId_found() throws Exception {
        Mockito.when(pedidoService.buscarPorId(1L)).thenReturn(Optional.of(samplePedido));
        Mockito.when(pedidoService.toResponse(samplePedido)).thenReturn(sampleResponseDTO);

        mockMvc.perform(get("/api/pedidos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(samplePedido.getId()))
                .andExpect(jsonPath("$.status").value(samplePedido.getStatus().toString()));
    }

    @Test
    void testBuscarPorId_notFound() throws Exception {
        Mockito.when(pedidoService.buscarPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/pedidos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSalvar() throws Exception {
        Mockito.when(pedidoService.salvar(any(PedidoRequestDTO.class))).thenReturn(samplePedido);
        Mockito.when(pedidoService.toResponse(samplePedido)).thenReturn(sampleResponseDTO);

        mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(samplePedido.getId()))
                .andExpect(jsonPath("$.status").value(samplePedido.getStatus().toString()));
    }

    @Test
    void testDeletar() throws Exception {
        Mockito.doNothing().when(pedidoService).deletar(1L);

        mockMvc.perform(delete("/api/pedidos/1"))
                .andExpect(status().isNoContent());
    }
}
