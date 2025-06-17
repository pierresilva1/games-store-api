package org.gamesstore.gamesstoreapi.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.gamesstore.gamesstoreapi.client.model.Client;
import org.gamesstore.gamesstoreapi.client.repository.ClientRepository;
import org.gamesstore.gamesstoreapi.order.dto.PedigreeRequestDTO;
import org.gamesstore.gamesstoreapi.order.model.OrderStatus;
import org.gamesstore.gamesstoreapi.order.model.Pedido;
import org.gamesstore.gamesstoreapi.order.repository.PedigreeRepository;
import org.gamesstore.gamesstoreapi.product.model.Product;
import org.gamesstore.gamesstoreapi.product.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@Tag(name = "Pedidos", description = "Endpoints de gerenciamento de pedidos")
public class PedidoController {

    private final PedigreeRepository pedigreeRepository;
    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;

    public PedidoController(PedigreeRepository pedigreeRepository, ProductRepository productRepository, ClientRepository clientRepository) {
        this.pedigreeRepository = pedigreeRepository;
        this.productRepository = productRepository;
        this.clientRepository = clientRepository;
    }

    @Operation(summary = "Lista todos os pedidos")
    @GetMapping
    public List<Pedido> listarTodos() {
        return pedigreeRepository.findAll();
    }

    @Operation(summary = "Busca um pedido pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) {
        return pedigreeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Cria um novo pedido")
    @PostMapping
    public ResponseEntity<Pedido> salvar(@Valid @RequestBody PedigreeRequestDTO dto) {
        Pedido pedido = new Pedido();


        Client cliente = clientRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        pedido.setClients(cliente);

        // Buscar produtos pelos IDs
        List<Product> produtos = productRepository.findAllById(dto.getProdutoIds());
        pedido.prosecutor(produtos);

        pedido.setStatus(OrderStatus.NOVO);
        pedido.setCreatedAt(LocalDateTime.now());

        Pedido salvo = pedigreeRepository.save(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }


    @Operation(summary = "Remove um pedido pelo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletar(@PathVariable Long id) {
        if(!pedigreeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        pedigreeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

