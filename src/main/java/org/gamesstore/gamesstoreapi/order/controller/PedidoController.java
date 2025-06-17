package org.gamesstore.gamesstoreapi.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.gamesstore.gamesstoreapi.order.dto.PedidoRequestDTO;
import org.gamesstore.gamesstoreapi.order.dto.PedidoResponseDTO;
import org.gamesstore.gamesstoreapi.order.model.Pedido;
import org.gamesstore.gamesstoreapi.order.service.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pedidos")
@Tag(name = "Pedidos", description = "Endpoints de gerenciamento de pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @Operation(summary = "Lista todos os pedidos")
    @GetMapping
    public List<PedidoResponseDTO> listarTodos() {
        return pedidoService.listarTodos().stream()
                .map(pedidoService::toResponse)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Busca um pedido pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> buscarPorId(@PathVariable Long id) {
        return pedidoService.buscarPorId(id)
                .map(pedido -> ResponseEntity.ok(pedidoService.toResponse(pedido)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Cria um novo pedido")
    @PostMapping
    public ResponseEntity<PedidoResponseDTO> salvar(@Valid @RequestBody PedidoRequestDTO dto) {
        Pedido pedidoSalvo = pedidoService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.toResponse(pedidoSalvo));
    }

    @Operation(summary = "Remove um pedido pelo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pedidoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
