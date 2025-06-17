package org.gamesstore.gamesstoreapi.product.service;

import org.gamesstore.gamesstoreapi.product.model.Product;
import org.gamesstore.gamesstoreapi.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    public ProductRepository productRepository;

    public Product salvar(Product product) {
        return productRepository.save(product);
    }

    public List<Product> buscarTodos() {
        return productRepository.findAll();
    }

    public Optional<Product> buscarId(Long id) {
        return productRepository.findById(id);
    }

    public Product actualizer(Product product) {
        if (product.getId() == null) {
            throw new IllegalArgumentException("Não é possível atualizar um produto sem ID");
        }
        return productRepository.save(product);
    }

    public void deletable(Long id) {
        productRepository.deleteById(id);
    }
}