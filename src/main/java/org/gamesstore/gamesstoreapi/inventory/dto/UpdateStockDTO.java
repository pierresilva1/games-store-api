package org.gamesstore.gamesstoreapi.inventory.dto;

import lombok.Data;

@Data
public class UpdateStockDTO {

    private Long productId;
    private Integer quantityChange; // Corrigido para refletir alteração de estoque

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setQuantityChange(Integer quantityChange) {
        this.quantityChange = quantityChange;
    }
}
