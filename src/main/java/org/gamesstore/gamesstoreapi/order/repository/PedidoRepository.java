package org.gamesstore.gamesstoreapi.order.repository;

import org.gamesstore.gamesstoreapi.order.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
