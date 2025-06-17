package org.gamesstore.gamesstoreapi.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PedigreeRequestDTO {

    @Schema(description = "ID do cliente que está fazendo o pedido", example = "42", required = true)
    @NotNull(message = "O ID do cliente é obrigatório.")
    private Long customerId;

    @Schema(description = "Lista de IDs dos produtos incluídos no pedido", example = "[101, 102]", required = true)
    @NotNull(message = "A lista de produtos não pode ser nula.")
    @Size(min = 1, message = "O pedido deve conter pelo menos um produto.")
    private List<Long> produtoIds;

    public PedigreeRequestDTO() {}

    public PedigreeRequestDTO(Long customerId, List<Long> produtoIds) {
        this.customerId = customerId;
        this.produtoIds = produtoIds;
    }

}
