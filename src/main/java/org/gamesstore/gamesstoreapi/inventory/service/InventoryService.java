package org.gamesstore.gamesstoreapi.inventory.service;

import org.gamesstore.gamesstoreapi.exceptions.ResourceNotFoundException;
import org.gamesstore.gamesstoreapi.inventory.dto.InventoryResponseDTO;
import org.gamesstore.gamesstoreapi.inventory.dto.UpdateStockDTO;
import org.gamesstore.gamesstoreapi.inventory.model.Inventory;
import org.gamesstore.gamesstoreapi.inventory.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository repository;

    public InventoryResponseDTO getStockByProductId(Long productId) {
        Inventory inventory = repository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado no estoque"));

        return new InventoryResponseDTO(inventory.getProductId(), inventory.getQuantity());
    }

    public void updateStock(UpdateStockDTO dto) {
        Inventory inventory = repository.findByProductId(dto.getProductId())
                .orElseGet(() -> new Inventory(dto.getProductId(), 0));

        int currentQuantity = inventory.getQuantity() != null ? inventory.getQuantity() : 0;
        int quantityChange = dto.getQuantityChange() != null ? dto.getQuantityChange() : 0;
        int newQuantity = currentQuantity + quantityChange;

        if (newQuantity < 0) {
            throw new RuntimeException("Estoque insuficiente para o produto ID " + dto.getProductId());
        }

        inventory.setQuantity(newQuantity);
        repository.save(inventory);
    }

    public void updateStock(Long productId, int quantityChange) {
        updateStock(new UpdateStockDTO(productId, quantityChange));
    }
}
