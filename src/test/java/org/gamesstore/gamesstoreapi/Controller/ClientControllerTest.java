package org.gamesstore.gamesstoreapi.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.gamesstore.gamesstoreapi.client.controller.ClientController;
import org.gamesstore.gamesstoreapi.client.dto.ClientRequestDTO;
import org.gamesstore.gamesstoreapi.client.dto.ClientResponseDTO;
import org.gamesstore.gamesstoreapi.client.dto.ClientUpdateDTO;
import org.gamesstore.gamesstoreapi.client.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClientController.class)
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @Autowired
    private ObjectMapper objectMapper;

    private ClientRequestDTO sampleRequestDto;
    private ClientResponseDTO sampleResponseDto;

    @BeforeEach
    void setup() {
        sampleRequestDto = new ClientRequestDTO();
        sampleRequestDto.setName("John Doe");
        sampleRequestDto.setEmail("john@example.com");
        sampleRequestDto.setTelefone("123456789");
        sampleRequestDto.setSenha("password123");

        sampleResponseDto = new ClientResponseDTO();
        sampleResponseDto.setId(1L);
        sampleResponseDto.setName("John Doe");
        sampleResponseDto.setEmail("john@example.com");
        sampleResponseDto.setTelefone("123456789");
    }

    @Test
    void testCreateClient() throws Exception {
        Mockito.when(clientService.saveClient(any(ClientRequestDTO.class))).thenReturn(sampleResponseDto);

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(sampleResponseDto.getId()))
                .andExpect(jsonPath("$.name").value(sampleResponseDto.getName()))
                .andExpect(jsonPath("$.email").value(sampleResponseDto.getEmail()));
    }

    @Test
    void testListClients() throws Exception {
        List<ClientResponseDTO> clients = List.of(sampleResponseDto);

        Mockito.when(clientService.listAllClients()).thenReturn(clients);

        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(sampleResponseDto.getId()))
                .andExpect(jsonPath("$[0].name").value(sampleResponseDto.getName()));
    }

    @Test
    void testGetClientById() throws Exception {
        Mockito.when(clientService.getClientById(1L)).thenReturn(sampleResponseDto);

        mockMvc.perform(get("/api/clients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleResponseDto.getId()))
                .andExpect(jsonPath("$.name").value(sampleResponseDto.getName()));
    }

    @Test
    void testUpdateClient() throws Exception {
        ClientUpdateDTO updateDto = new ClientUpdateDTO();
        updateDto.setName("John Updated");
        updateDto.setEmail("john.updated@example.com");
        updateDto.setTelefone("987654321");

        ClientResponseDTO updatedClient = new ClientResponseDTO();
        updatedClient.setId(1L);
        updatedClient.setName("John Updated");
        updatedClient.setEmail("john.updated@example.com");
        updatedClient.setTelefone("987654321");

        Mockito.when(clientService.updateClient(eq(1L), any(ClientUpdateDTO.class))).thenReturn(updatedClient);

        mockMvc.perform(put("/api/clients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedClient.getId()))
                .andExpect(jsonPath("$.name").value("John Updated"))
                .andExpect(jsonPath("$.email").value("john.updated@example.com"));
    }

    @Test
    void testDeleteClient() throws Exception {
        Mockito.doNothing().when(clientService).deleteClient(1L);

        mockMvc.perform(delete("/api/clients/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testSearchClientsByEmail() throws Exception {
        List<ClientResponseDTO> clients = List.of(sampleResponseDto);

        Mockito.when(clientService.searchClientsByEmail("john@example.com")).thenReturn(clients);

        mockMvc.perform(get("/api/clients/search")
                        .param("email", "john@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("john@example.com"));
    }
}
