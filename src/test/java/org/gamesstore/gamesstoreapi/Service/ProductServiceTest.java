package org.gamesstore.gamesstoreapi.Service;

import org.gamesstore.gamesstoreapi.product.model.Product;
import org.gamesstore.gamesstoreapi.product.repository.ProductRepository;
import org.gamesstore.gamesstoreapi.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
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
    public void testSave() {
        Product product = new Product();
        product.setName("Game Test");
        product.setPrice(BigDecimal.TEN);

        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.save(product);

        assertNotNull(result);
        assertEquals(product, result);
        verify(productRepository).save(product);
    }

    @Test
    public void testFindAll() {
        Product p1 = new Product();
        p1.setId(1L);
        p1.setName("Game 1");

        Product p2 = new Product();
        p2.setId(2L);
        p2.setName("Game 2");

        when(productRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Product> allProducts = productService.findAll();

        assertEquals(2, allProducts.size());
        assertTrue(allProducts.contains(p1));
        assertTrue(allProducts.contains(p2));
        verify(productRepository).findAll();
    }

    @Test
    public void testFindById_Found() {
        Product product = new Product();
        product.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Optional<Product> result = productService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(productRepository).findById(1L);
    }

    @Test
    public void testFindById_NotFound() {
        when(productRepository.findById(10L)).thenReturn(Optional.empty());

        Optional<Product> result = productService.findById(10L);

        assertFalse(result.isPresent());
        verify(productRepository).findById(10L);
    }

    @Test
    public void testUpdate_Success() {
        Product product = new Product();
        product.setName("Updated Game");
        product.setPrice(BigDecimal.valueOf(150));

        Product existing = new Product();
        existing.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product updated = productService.update(1L, product);

        assertEquals(1L, updated.getId());
        assertEquals("Updated Game", updated.getName());
        verify(productRepository).findById(1L);
        verify(productRepository).save(product);
    }

    @Test
    public void testUpdate_NotFound() {
        Product product = new Product();
        product.setName("Updated");

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.update(1L, product);
        });

        assertEquals("Produto com ID 1 não encontrado.", exception.getMessage());
        verify(productRepository).findById(1L);
        verify(productRepository, never()).save(any());
    }

    @Test
    public void testDelete_Success() {
        when(productRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1L);

        productService.delete(1L);

        verify(productRepository).existsById(1L);
        verify(productRepository).deleteById(1L);
    }

    @Test
    public void testDelete_NotFound() {
        when(productRepository.existsById(1L)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.delete(1L);
        });

        assertEquals("Produto com ID 1 não encontrado.", exception.getMessage());
        verify(productRepository).existsById(1L);
        verify(productRepository, never()).deleteById(any());
    }
}
