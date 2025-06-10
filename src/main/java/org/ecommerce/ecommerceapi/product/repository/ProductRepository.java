package org.ecommerce.ecommerceapi.product.repository;More actions

import org.ecommerce.ecommerceapi.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@RepositoryMore actions
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Aqui podemos adicionar métodos de consulta personalizados se necessário
}