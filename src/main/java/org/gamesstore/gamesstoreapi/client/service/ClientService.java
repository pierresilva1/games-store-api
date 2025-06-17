package org.gamesstore.gamesstoreapi.client.service;

import jakarta.validation.Valid;
import org.gamesstore.gamesstoreapi.client.dto.ClientRequestDTO;
import org.gamesstore.gamesstoreapi.client.dto.ClientResponseDTO;
import org.gamesstore.gamesstoreapi.client.dto.ClientUpdateDTO;
import org.gamesstore.gamesstoreapi.client.model.Client;
import org.gamesstore.gamesstoreapi.client.repository.ClientRepository;
import org.gamesstore.gamesstoreapi.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ClientResponseDTO saveClient(ClientRequestDTO dto) {
        Client client = new Client();
        client.setName(dto.getName());
        client.setEmail(dto.getEmail());
        client.setTelefone(dto.getTelefone());
        client.setSenha(passwordEncoder.encode(dto.getSenha()));
        return toResponseDTO(clientRepository.save(client));
    }

    public List<ClientResponseDTO> listAllClients() {
        return clientRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ClientResponseDTO getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com id " + id));
        return toResponseDTO(client);
    }

    public ClientResponseDTO updateClient(Long id, @Valid ClientUpdateDTO dto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com id " + id));

        if (dto.getName() != null) client.setName(dto.getName());
        if (dto.getEmail() != null) client.setEmail(dto.getEmail());
        if (dto.getTelefone() != null) client.setTelefone(dto.getTelefone());
        if (dto.getSenha() != null && !dto.getSenha().isEmpty()) {
            client.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        return toResponseDTO(clientRepository.save(client));
    }

    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com id " + id));
        clientRepository.delete(client);
    }

    public List<ClientResponseDTO> searchClientsByEmail(String email) {
        return clientRepository.findByEmailContainingIgnoreCase(email)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private ClientResponseDTO toResponseDTO(Client client) {
        return new ClientResponseDTO(
                client.getId(),
                client.getName(),
                client.getEmail(),
                client.getTelefone()
        );
    }
}
