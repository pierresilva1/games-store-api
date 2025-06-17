package org.gamesstore.gamesstoreapi.order.service;

import org.gamesstore.gamesstoreapi.client.model.Client;
import org.gamesstore.gamesstoreapi.client.repository.ClientRepository;
import org.gamesstore.gamesstoreapi.order.dto.PedidoRequestDTO;
import org.gamesstore.gamesstoreapi.order.dto.PedidoResponseDTO;
import org.gamesstore.gamesstoreapi.order.model.OrderStatus;
import org.gamesstore.gamesstoreapi.order.model.Pedido;
import org.gamesstore.gamesstoreapi.order.repository.PedidoRepository;
import org.gamesstore.gamesstoreapi.product.model.Product;
import org.gamesstore.gamesstoreapi.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;

    public PedidoService(PedidoRepository pedidoRepository, ProductRepository productRepository, ClientRepository clientRepository) {
        this.pedidoRepository = pedidoRepository;
        this.productRepository = productRepository;
        this.clientRepository = clientRepository;
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    public Pedido salvar(PedidoRequestDTO dto) {
        Client cliente = clientRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        List<Product> produtos = productRepository.findAllById(dto.getProdutoIds());

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setProdutos(produtos);
        pedido.setStatus(OrderStatus.NOVO);

        return pedidoRepository.save(pedido);
    }

    public void deletar(Long id) {
        if (!pedidoRepository.existsById(id)) {
            throw new RuntimeException("Pedido não encontrado");
        }
        pedidoRepository.deleteById(id);
    }

    public PedidoResponseDTO toResponse(Pedido pedido) {
        return PedidoResponseDTO.builder()
                .pedidoId(pedido.getId())
                .clienteId(pedido.getCliente().getId())
                .produtoIds(pedido.getProdutos().stream().map(Product::getId).collect(Collectors.toList()))
                .status(OrderStatus.valueOf(pedido.getStatus().name()))
                .build();
    }
}
