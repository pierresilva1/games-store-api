package org.gamesstore.gamesstoreapi.inventory.controller;

import org.gamesstore.gamesstoreapi.inventory.dto.InventoryResponseDTO;
import org.gamesstore.gamesstoreapi.inventory.dto.UpdateStockDTO;
import org.gamesstore.gamesstoreapi.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/estoque")
public class InventoryController {

    @Autowired
    private InventoryService service;

    @GetMapping("/{productId}")
    public InventoryResponseDTO getStock(@PathVariable Long productId) {
        return service.getStockByProductId(productId);
    }

    @PostMapping("/update")
    public void updateStock(@RequestBody UpdateStockDTO dto) {
        service.updateStock(dto);
    }
}
