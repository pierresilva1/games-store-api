package org.gamesstore.gamesstoreapi.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.gamesstore.gamesstoreapi.inventory.controller.InventoryController;
import org.gamesstore.gamesstoreapi.inventory.dto.InventoryResponseDTO;
import org.gamesstore.gamesstoreapi.inventory.dto.UpdateStockDTO;
import org.gamesstore.gamesstoreapi.inventory.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventoryController.class)
public class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryService service;

    @Autowired
    private ObjectMapper objectMapper;

    private InventoryResponseDTO sampleInventoryResponseDTO;
    private UpdateStockDTO sampleUpdateStockDTO;

    @BeforeEach
    void setup() {
        sampleInventoryResponseDTO = new InventoryResponseDTO();
        sampleInventoryResponseDTO.setProductId(1L);
        sampleInventoryResponseDTO.setQuantity(100);

        sampleUpdateStockDTO = new UpdateStockDTO();
        sampleUpdateStockDTO.setProductId(1L);
        sampleUpdateStockDTO.setQuantityChange(50);
    }

    @Test
    void testGetStock() throws Exception {
        Mockito.when(service.getStockByProductId(1L)).thenReturn(sampleInventoryResponseDTO);

        mockMvc.perform(get("/api/estoque/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(sampleInventoryResponseDTO.getProductId()))
                .andExpect(jsonPath("$.quantity").value(sampleInventoryResponseDTO.getQuantity()));
    }

    @Test
    void testUpdateStock() throws Exception {
        Mockito.doNothing().when(service).updateStock(any(UpdateStockDTO.class));

        mockMvc.perform(post("/api/estoque/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleUpdateStockDTO)))
                .andExpect(status().isOk());
    }
}
