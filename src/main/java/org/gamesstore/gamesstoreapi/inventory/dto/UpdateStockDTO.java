package org.gamesstore.gamesstoreapi.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStockDTO {
    private Long productId;
    private Integer quantityChange;
}
