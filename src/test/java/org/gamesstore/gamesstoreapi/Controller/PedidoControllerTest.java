package org.gamesstore.gamesstoreapi.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.gamesstore.gamesstoreapi.client.model.Client;
import org.gamesstore.gamesstoreapi.client.repository.ClientRepository;
import org.gamesstore.gamesstoreapi.order.controller.PedidoController;
import org.gamesstore.gamesstoreapi.order.dto.PedigreeRequestDTO;
import org.gamesstore.gamesstoreapi.order.model.OrderStatus;
import org.gamesstore.gamesstoreapi.order.model.Pedido;
import org.gamesstore.gamesstoreapi.order.repository.PedigreeRepository;
import org.gamesstore.gamesstoreapi.product.model.Product;
import org.gamesstore.gamesstoreapi.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PedidoController.class)
public class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedigreeRepository pedigreeRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private ClientRepository clientRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Client sampleClient;
    private Product sampleProduct1;
    private Product sampleProduct2;
    private Pedido samplePedido;

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
        samplePedido.setClients(sampleClient);
        samplePedido.prosecutor(List.of(sampleProduct1, sampleProduct2));
        samplePedido.setStatus(OrderStatus.NOVO);
        samplePedido.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testListarTodos() throws Exception {
        List<Pedido> pedidos = List.of(samplePedido);
        Mockito.when(pedigreeRepository.findAll()).thenReturn(pedidos);

        mockMvc.perform(get("/api/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(samplePedido.getId()))
                .andExpect(jsonPath("$[0].status").value(samplePedido.getStatus().toString()));
    }

    @Test
    void testBuscarPorId_found() throws Exception {
        Mockito.when(pedigreeRepository.findById(1L)).thenReturn(Optional.of(samplePedido));

        mockMvc.perform(get("/api/pedidos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(samplePedido.getId()))
                .andExpect(jsonPath("$.status").value(samplePedido.getStatus().toString()));
    }

    @Test
    void testBuscarPorId_notFound() throws Exception {
        Mockito.when(pedigreeRepository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/pedidos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSalvar() throws Exception {
        PedigreeRequestDTO requestDTO = new PedigreeRequestDTO();
        requestDTO.setCustomerId(sampleClient.getId());
        requestDTO.setProdutoIds(Arrays.asList(sampleProduct1.getId(), sampleProduct2.getId()));

        Mockito.when(clientRepository.findById(sampleClient.getId())).thenReturn(Optional.of(sampleClient));
        Mockito.when(productRepository.findAllById(requestDTO.getProdutoIds())).thenReturn(List.of(sampleProduct1, sampleProduct2));
        Mockito.when(pedigreeRepository.save(any(Pedido.class))).thenAnswer(i -> {
            Pedido p = i.getArgument(0);
            p.setId(1L);
            return p;
        });

        mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value(OrderStatus.NOVO.toString()))
                .andExpect(jsonPath("$.clients.id").value(sampleClient.getId()));
    }

    @Test
    void testSalvar_clientNotFound() throws Exception {
        PedigreeRequestDTO requestDTO = new PedigreeRequestDTO();
        requestDTO.setCustomerId(99L);
        requestDTO.setProdutoIds(Arrays.asList(sampleProduct1.getId()));

        Mockito.when(clientRepository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isInternalServerError()); // Porque o controller lança RuntimeException
    }

    @Test
    void testDeletar_found() throws Exception {
        Mockito.when(pedigreeRepository.existsById(1L)).thenReturn(true);
        Mockito.doNothing().when(pedigreeRepository).deleteById(1L);

        mockMvc.perform(delete("/api/pedidos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeletar_notFound() throws Exception {
        Mockito.when(pedigreeRepository.existsById(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/pedidos/99"))
                .andExpect(status().isNotFound());
    }
}
