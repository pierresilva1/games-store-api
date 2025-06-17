package org.gamesstore.gamesstoreapi.inventory.controller;

import org.gamesstore.gamesstoreapi.inventory.dto.InventoryResponseDTO;
import org.gamesstore.gamesstoreapi.inventory.dto.UpdateStockDTO;
import org.gamesstore.gamesstoreapi.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryService service;

    @GetMapping("/{productId}")
    public ResponseEntity<InventoryResponseDTO> getStock(@PathVariable Long productId) {
        InventoryResponseDTO dto = service.getStockByProductId(productId);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/update")
    public ResponseEntity<Void> updateStock(@RequestBody UpdateStockDTO dto) {
        service.updateStock(dto);
        return ResponseEntity.ok().build();
    }
}
