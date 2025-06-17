package org.gamesstore.gamesstoreapi;

import org.gamesstore.gamesstoreapi.client.dto.ClientRequestDTO;
import org.gamesstore.gamesstoreapi.client.dto.ClientResponseDTO;
import org.gamesstore.gamesstoreapi.client.model.Client;
import org.gamesstore.gamesstoreapi.client.repository.ClientRepository;
import org.gamesstore.gamesstoreapi.client.service.ClientService;
import org.gamesstore.gamesstoreapi.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private ClientRequestDTO createClientRequestDTO() {
        ClientRequestDTO dto = new ClientRequestDTO();
        dto.setName("John Doe");
        dto.setEmail("john@example.com");
        dto.setTelefone("123456789");
        dto.setSenha("password");
        return dto;
    }

    private Client createClient(Long id) {
        Client client = new Client();
        client.setId(id);
        client.setName("John Doe");
        client.setEmail("john@example.com");
        client.setTelefone("123456789");
        client.setSenha("hashedPassword");
        return client;
    }

    @Test
    void shouldSaveClient() {
        ClientRequestDTO dto = createClientRequestDTO();
        Client client = createClient(1L);

        when(passwordEncoder.encode("password")).thenReturn("hashedPassword");
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        ClientResponseDTO response = clientService.saveClient(dto);

        assertNotNull(response);
        assertEquals("John Doe", response.getName());
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    void shouldListAllClients() {
        List<Client> clients = Arrays.asList(createClient(1L), createClient(2L));
        when(clientRepository.findAll()).thenReturn(clients);

        List<ClientResponseDTO> result = clientService.listAllClient();

        assertEquals(2, result.size());
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    void shouldFindClientById() {
        Client client = createClient(1L);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        ClientResponseDTO result = clientService.searchForIdClient(1L);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
    }

    @Test
    void shouldThrowExceptionWhenClientNotFoundById() {
        when(clientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clientService.searchForIdClient(99L));
    }

    @Test
    void shouldUpdateClient() {
        Client existingClient = createClient(1L);
        ClientRequestDTO dto = createClientRequestDTO();

        when(clientRepository.findById(1L)).thenReturn(Optional.of(existingClient));
        when(passwordEncoder.encode("password")).thenReturn("hashedPassword");
        when(clientRepository.save(any(Client.class))).thenReturn(existingClient);

        ClientResponseDTO result = clientService.updateClient(1L, dto);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(clientRepository, times(1)).save(existingClient);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonexistentClient() {
        ClientRequestDTO dto = createClientRequestDTO();
        when(clientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clientService.updateClient(99L, dto));
    }

    @Test
    void shouldDeleteClient() {
        Client client = createClient(1L);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        doNothing().when(clientRepository).delete(client);

        clientService.deleteClient(1L);

        verify(clientRepository, times(1)).delete(client);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonexistentClient() {
        when(clientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clientService.deleteClient(99L));
    }

    @Test
    void shouldSearchClientByEmail() {
        Client client1 = createClient(1L);
        Client client2 = createClient(2L);
        client2.setEmail("another@example.com");

        when(clientRepository.findAll()).thenReturn(Arrays.asList(client1, client2));

        List<ClientResponseDTO> result = clientService.searchForEmail("john@example.com");

        assertEquals(1, result.size());
        assertEquals("john@example.com", result.get(0).getEmail());
    }
}
