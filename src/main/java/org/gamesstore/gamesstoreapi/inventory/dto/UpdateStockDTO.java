package org.gamesstore.gamesstoreapi.inventory.dto;

public class UpdateStockDTO {
    private Long productId;
    private Integer quantityChange;


    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantityChange() {
        return quantityChange;
    }

    public void setQuantityChange(Integer quantityChange) {
        this.quantityChange = quantityChange;
    }
}