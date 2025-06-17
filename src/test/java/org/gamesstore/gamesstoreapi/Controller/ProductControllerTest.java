package org.gamesstore.gamesstoreapi.Controller;

import org.gamesstore.gamesstoreapi.product.controller.ProductController;
import org.gamesstore.gamesstoreapi.product.model.Product;
import org.gamesstore.gamesstoreapi.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.any;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private Product sampleProduct;

    @BeforeEach
    void setup() {
        sampleProduct = new Product();
        sampleProduct.setId(1L);
        sampleProduct.setName("Game Example");
        sampleProduct.setDescription("Description Example");
        sampleProduct.setPrice(BigDecimal.valueOf(100.0));
    }

    @Test
    void testCreateProduct() throws Exception {
        Mockito.when(productService.save(any(Product.class))).thenReturn(sampleProduct);

        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleProduct)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(sampleProduct.getId()))
                .andExpect(jsonPath("$.name").value(sampleProduct.getName()));
    }

    @Test
    void testListProducts() throws Exception {
        Mockito.when(productService.findAll()).thenReturn(Arrays.asList(sampleProduct));

        mockMvc.perform(get("/api/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(sampleProduct.getId()))
                .andExpect(jsonPath("$[0].name").value(sampleProduct.getName()));
    }

    @Test
    void testSearchProductById_Found() throws Exception {
        Mockito.when(productService.findById(1L)).thenReturn(Optional.of(sampleProduct));

        mockMvc.perform(get("/api/produtos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleProduct.getId()))
                .andExpect(jsonPath("$.name").value(sampleProduct.getName()));
    }

    @Test
    void testSearchProductById_NotFound() throws Exception {
        Mockito.when(productService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/produtos/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateProduct() throws Exception {
        Product updatedProduct = new Product();
        updatedProduct.setId(1L);
        updatedProduct.setName("Updated Game");
        updatedProduct.setDescription("Updated Description");
        updatedProduct.setPrice(BigDecimal.valueOf(150.0));

        Mockito.when(productService.update(any(Long.class), any(Product.class))).thenReturn(updatedProduct);

        mockMvc.perform(put("/api/produtos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedProduct.getId()))
                .andExpect(jsonPath("$.name").value("Updated Game"));
    }

    @Test
    void testDeleteProduct() throws Exception {
        Mockito.doNothing().when(productService).delete(1L);

        mockMvc.perform(delete("/api/produtos/1"))
                .andExpect(status().isNoContent());
    }
}
