package org.gamesstore.gamesstoreapi.client.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.gamesstore.gamesstoreapi.client.dto.ClientRequestDTO;
import org.gamesstore.gamesstoreapi.client.dto.ClientResponseDTO;
import org.gamesstore.gamesstoreapi.client.dto.ClientUpdateDTO;
import org.gamesstore.gamesstoreapi.client.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@OpenAPIDefinition(
        info = @Info(
                title = "Games Store API",
                version = "1.0",
                description = "Documentação da API de Clientes"
        ),
        servers = {
                @Server(url = "https://games-store-api-1.onrender.com", description = "Servidor Render")
        }
)
@RestController
@RequestMapping("/api/clients")
@Tag(name = "Clients", description = "Endpoints para gerenciamento de clientes")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Operation(summary = "Cria um novo cliente")
    @PostMapping
    public ResponseEntity<ClientResponseDTO> createClient(@Valid @RequestBody ClientRequestDTO dto) {
        ClientResponseDTO createdClient = clientService.saveClient(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdClient);
    }

    @Operation(summary = "Lista todos os clientes")
    @GetMapping
    public ResponseEntity<List<ClientResponseDTO>> listClients() {
        List<ClientResponseDTO> clients = clientService.listAllClients();
        return ResponseEntity.ok(clients);
    }

    @Operation(summary = "Busca um cliente pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> getClientById(@PathVariable Long id) {
        ClientResponseDTO client = clientService.getClientById(id);
        return ResponseEntity.ok(client);
    }

    @Operation(summary = "Atualiza um cliente existente")
    @PutMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> updateClient(
            @PathVariable Long id,
            @Valid @RequestBody ClientUpdateDTO dto) {
        ClientResponseDTO updatedClient = clientService.updateClient(id, dto);
        return ResponseEntity.ok(updatedClient);
    }

    @Operation(summary = "Remove um cliente pelo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Busca clientes por email")
    @GetMapping("/search")
    public ResponseEntity<List<ClientResponseDTO>> searchClientsByEmail(@RequestParam String email) {
        List<ClientResponseDTO> clients = clientService.searchClientsByEmail(email);
        return ResponseEntity.ok(clients);
    }
}
