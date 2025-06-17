package org.gamesstore.gamesstoreapi;

import org.gamesstore.gamesstoreapi.product.model.Product;
import org.gamesstore.gamesstoreapi.product.repository.ProductRepository;
import org.gamesstore.gamesstoreapi.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    private ProductRepository productRepository;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        productService = new ProductService();
        productService.productRepository = productRepository; // Injeção manual
    }

    private Product createProduct(Long id, int estoque) {
        Product p = new Product();
        p.setId(id);
        p.setName("Produto Teste");
        p.setQuantidadeEstoque(estoque);
        return p;
    }

    @Test
    void shouldSaveProduct() {
        Product product = createProduct(null, 10);
        Product savedProduct = createProduct(1L, 10);

        when(productRepository.save(product)).thenReturn(savedProduct);

        Product result = productService.salvar(product);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(productRepository).save(product);
    }

    @Test
    void shouldReturnAllProducts() {
        List<Product> products = Arrays.asList(createProduct(1L, 5), createProduct(2L, 8));
        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.buscarTodos();

        assertEquals(2, result.size());
        verify(productRepository).findAll();
    }

    @Test
    void shouldReturnProductById() {
        Product product = createProduct(1L, 5);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Optional<Product> result = productService.buscarId(1L);

        assertTrue(result.isPresent());
        assertEquals("Produto Teste", result.get().getName());
    }

    @Test
    void shouldUpdateProductWithId() {
        Product product = createProduct(1L, 20);

        when(productRepository.save(product)).thenReturn(product);

        Product updated = productService.actualizer(product);

        assertNotNull(updated);
        assertEquals(20, updated.getQuantidadeEstoque());
        verify(productRepository).save(product);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWithoutId() {
        Product product = createProduct(null, 5);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> productService.actualizer(product));
        assertEquals("Não é possível atualizar um produto sem ID", exception.getMessage());
    }

    @Test
    void shouldDeleteProductById() {
        doNothing().when(productRepository).deleteById(1L);

        productService.deletable(1L);

        verify(productRepository).deleteById(1L);
    }

    @Test
    void shouldReturnTrueWhenStockIsSufficient() {
        Product product = createProduct(1L, 10);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        boolean available = productService.verificarEstoque(1L, 5);

        assertTrue(available);
    }

    @Test
    void shouldReturnFalseWhenStockIsInsufficient() {
        Product product = createProduct(1L, 2);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        boolean available = productService.verificarEstoque(1L, 5);

        assertFalse(available);
    }

    @Test
    void shouldReturnFalseWhenProductNotFoundInStockCheck() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        boolean available = productService.verificarEstoque(99L, 5);

        assertFalse(available);
    }

    @Test
    void shouldUpdateStockSuccessfully() {
        Product product = createProduct(1L, 10);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any())).thenReturn(product);

        productService.atualizarEstoque(1L, 4);

        assertEquals(6, product.getQuantidadeEstoque());
        verify(productRepository).save(product);
    }

    @Test
    void shouldThrowWhenStockNotEnough() {
        Product product = createProduct(1L, 3);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Exception exception = assertThrows(RuntimeException.class,
                () -> productService.atualizarEstoque(1L, 5));

        assertTrue(exception.getMessage().contains("Estoque insuficiente"));
    }

    @Test
    void shouldThrowWhenProductNotFoundInStockUpdate() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class,
                () -> productService.atualizarEstoque(1L, 2));

        assertTrue(exception.getMessage().contains("Produto não encontrado"));
    }

    @Test
    void shouldThrowWhenStockIsNull() {
        Product product = createProduct(1L, 0);
        product.setQuantidadeEstoque(null);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Exception exception = assertThrows(RuntimeException.class,
                () -> productService.atualizarEstoque(1L, 2));

        assertTrue(exception.getMessage().contains("Estoque do produto não está definido"));
    }
}
