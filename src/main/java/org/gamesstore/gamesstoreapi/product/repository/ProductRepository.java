package org.gamesstore.gamesstoreapi.product.repository;

import org.gamesstore.gamesstoreapi.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Métodos personalizados podem ser adicionados aqui se necessário
}
