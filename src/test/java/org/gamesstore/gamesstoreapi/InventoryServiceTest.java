package org.gamesstore.gamesstoreapi;


import org.gamesstore.gamesstoreapi.inventory.dto.InventoryResponseDTO;
import org.gamesstore.gamesstoreapi.inventory.dto.UpdateStockDTO;
import org.gamesstore.gamesstoreapi.inventory.model.Inventory;
import org.gamesstore.gamesstoreapi.inventory.repository.InventoryRepository;
import org.gamesstore.gamesstoreapi.inventory.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InventoryServiceTest {

    private InventoryRepository inventoryRepository;
    private InventoryService inventoryService;

    @BeforeEach
    void setUp() {
        inventoryRepository = mock(InventoryRepository.class);
        inventoryService = new InventoryService();
        inventoryService.setRepository(inventoryRepository);
    }

    @Test
    void shouldReturnStockByProductId() {
        Inventory inventory = new Inventory();
        inventory.setProductId(1L);
        inventory.setQuantity(15);

        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.of(inventory));

        InventoryResponseDTO result = inventoryService.getStockByProductId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getProductId());
        assertEquals(15, result.getQuantity());
        verify(inventoryRepository).findByProductId(1L);
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        when(inventoryRepository.findByProductId(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                inventoryService.getStockByProductId(99L));

        assertEquals("Produto não encontrado no estoque", exception.getMessage());
        verify(inventoryRepository).findByProductId(99L);
    }

    @Test
    void shouldUpdateStockWhenProductExists() {
        Inventory inventory = new Inventory();
        inventory.setProductId(1L);
        inventory.setQuantity(10);

        UpdateStockDTO dto = new UpdateStockDTO();
        dto.setProductId(1L);
        dto.setQuantityChange(5);

        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.of(inventory));

        inventoryService.updateStock(dto);

        assertEquals(15, inventory.getQuantity());
        verify(inventoryRepository).save(inventory);
    }

    @Test
    void shouldCreateInventoryWhenProductNotFound() {
        UpdateStockDTO dto = new UpdateStockDTO();
        dto.setProductId(2L);
        dto.setQuantityChange(8);

        when(inventoryRepository.findByProductId(2L)).thenReturn(Optional.empty());

        inventoryService.updateStock(dto);

        verify(inventoryRepository).save(argThat(inv ->
                inv.getProductId().equals(2L) && inv.getQuantity() == 8
        ));
    }

    @Test
    void shouldThrowExceptionWhenResultingStockIsNegative() {
        Inventory inventory = new Inventory();
        inventory.setProductId(3L);
        inventory.setQuantity(3);

        UpdateStockDTO dto = new UpdateStockDTO();
        dto.setProductId(3L);
        dto.setQuantityChange(-5);

        when(inventoryRepository.findByProductId(3L)).thenReturn(Optional.of(inventory));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                inventoryService.updateStock(dto));

        assertEquals("Estoque insuficiente", exception.getMessage());
    }

    @Test
    void shouldUpdateStockWithPrimitiveMethod() {
        Inventory inventory = new Inventory();
        inventory.setProductId(4L);
        inventory.setQuantity(20);

        when(inventoryRepository.findByProductId(4L)).thenReturn(Optional.of(inventory));

        inventoryService.updateStock(4L, -5);

        assertEquals(15, inventory.getQuantity());
        verify(inventoryRepository).save(inventory);
    }
}
