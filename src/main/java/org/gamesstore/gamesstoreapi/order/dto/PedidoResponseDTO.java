package org.gamesstore.gamesstoreapi.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gamesstore.gamesstoreapi.client.model.Client;
import org.gamesstore.gamesstoreapi.product.model.Product;
import org.gamesstore.gamesstoreapi.order.model.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PedidoResponseDTO {

    private Long pedidoId;
    private Client cliente;
    private List<Product> produtos;
    private OrderStatus status;
    private LocalDateTime dataCriacao;
}
