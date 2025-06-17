package org.gamesstore.gamesstoreapi.client.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ClientUpdateDTO {

    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    private String name;

    @Email(message = "Deve ser um e-mail válido")
    private String email;

    @Pattern(regexp = "\\d{10,15}", message = "O telefone deve ter entre 10 e 15 dígitos numéricos")
    private String telefone;

    @Size(min = 6, max = 100, message = "A senha deve ter entre 6 e 100 caracteres")
    private String senha;
}
