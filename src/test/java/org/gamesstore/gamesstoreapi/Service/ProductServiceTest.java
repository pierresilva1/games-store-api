package org.gamesstore.gamesstoreapi.Service;

import org.gamesstore.gamesstoreapi.product.model.Product;
import org.gamesstore.gamesstoreapi.product.repository.ProductRepository;
import org.gamesstore.gamesstoreapi.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    public void testSalvar() {
        Product product = new Product();
        product.setId(null);
        product.setName("Game Test");

        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.salvar(product);

        assertNotNull(result);
        assertEquals(product, result);
        verify(productRepository).save(product);
    }

    @Test
    public void testBuscarTodos() {
        Product p1 = new Product();
        p1.setId(1L);
        p1.setName("Game 1");

        Product p2 = new Product();
        p2.setId(2L);
        p2.setName("Game 2");

        when(productRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Product> allProducts = productService.buscarTodos();

        assertEquals(2, allProducts.size());
        assertTrue(allProducts.contains(p1));
        assertTrue(allProducts.contains(p2));
        verify(productRepository).findAll();
    }

    @Test
    public void testBuscarId_Found() {
        Product product = new Product();
        product.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Optional<Product> result = productService.buscarId(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(productRepository).findById(1L);
    }

    @Test
    public void testBuscarId_NotFound() {
        when(productRepository.findById(10L)).thenReturn(Optional.empty());

        Optional<Product> result = productService.buscarId(10L);

        assertFalse(result.isPresent());
        verify(productRepository).findById(10L);
    }

    @Test
    public void testActualizer_WithId() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Updated Game");

        when(productRepository.save(product)).thenReturn(product);

        Product updated = productService.actualizer(product);

        assertEquals(product, updated);
        verify(productRepository).save(product);
    }

    @Test
    public void testActualizer_WithoutId_ShouldThrow() {
        Product product = new Product();
        product.setId(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.actualizer(product);
        });

        assertEquals("Não é possível atualizar um produto sem ID", exception.getMessage());
        verify(productRepository, never()).save(any());
    }

    @Test
    public void testDeletable() {
        doNothing().when(productRepository).deleteById(1L);

        productService.deletable(1L);

        verify(productRepository).deleteById(1L);
    }
}
