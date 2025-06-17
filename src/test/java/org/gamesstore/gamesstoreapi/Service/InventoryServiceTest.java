package org.gamesstore.gamesstoreapi.Service;


import org.gamesstore.gamesstoreapi.inventory.dto.InventoryResponseDTO;
import org.gamesstore.gamesstoreapi.inventory.dto.UpdateStockDTO;
import org.gamesstore.gamesstoreapi.inventory.model.Inventory;
import org.gamesstore.gamesstoreapi.inventory.repository.InventoryRepository;
import org.gamesstore.gamesstoreapi.inventory.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceTest {

    @Mock
    private InventoryRepository repository;

    @InjectMocks
    private InventoryService inventoryService;

    @Test
    public void testGetStockByProductId_Found() {
        Inventory inventory = new Inventory();
        inventory.setProductId(1L);
        inventory.setQuantity(50);

        when(repository.findByProductId(1L)).thenReturn(Optional.of(inventory));

        InventoryResponseDTO dto = inventoryService.getStockByProductId(1L);

        assertEquals(1L, dto.getProductId());
        assertEquals(50, dto.getQuantity());
        verify(repository).findByProductId(1L);
    }

    @Test
    public void testGetStockByProductId_NotFound() {
        when(repository.findByProductId(2L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            inventoryService.getStockByProductId(2L);
        });

        assertEquals("Produto não encontrado no estoque", ex.getMessage());
    }

    @Test
    public void testUpdateStock_NewInventory() {
        UpdateStockDTO dto = new UpdateStockDTO();
        dto.setProductId(3L);
        dto.setQuantityChange(10);

        when(repository.findByProductId(3L)).thenReturn(Optional.empty());
        when(repository.save(any(Inventory.class))).thenAnswer(i -> i.getArgument(0));

        inventoryService.updateStock(dto);

        ArgumentCaptor<Inventory> captor = ArgumentCaptor.forClass(Inventory.class);
        verify(repository).save(captor.capture());

        Inventory saved = captor.getValue();
        assertEquals(3L, saved.getProductId());
        assertEquals(10, saved.getQuantity());
    }

    @Test
    public void testUpdateStock_ExistingInventory_PositiveChange() {
        Inventory existing = new Inventory();
        existing.setProductId(4L);
        existing.setQuantity(5);

        UpdateStockDTO dto = new UpdateStockDTO();
        dto.setProductId(4L);
        dto.setQuantityChange(3);

        when(repository.findByProductId(4L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Inventory.class))).thenAnswer(i -> i.getArgument(0));

        inventoryService.updateStock(dto);

        ArgumentCaptor<Inventory> captor = ArgumentCaptor.forClass(Inventory.class);
        verify(repository).save(captor.capture());

        Inventory saved = captor.getValue();
        assertEquals(4L, saved.getProductId());
        assertEquals(8, saved.getQuantity());
    }

    @Test
    public void testUpdateStock_InsufficientStock() {
        Inventory existing = new Inventory();
        existing.setProductId(5L);
        existing.setQuantity(2);

        UpdateStockDTO dto = new UpdateStockDTO();
        dto.setProductId(5L);
        dto.setQuantityChange(-5);

        when(repository.findByProductId(5L)).thenReturn(Optional.of(existing));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            inventoryService.updateStock(dto);
        });

        assertEquals("Estoque insuficiente", ex.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    public void testUpdateStock_UsingOverload() {
        when(repository.findByProductId(6L)).thenReturn(Optional.empty());
        when(repository.save(any(Inventory.class))).thenAnswer(i -> i.getArgument(0));

        inventoryService.updateStock(6L, 15);

        verify(repository).save(any(Inventory.class));
    }
}
