package org.gamesstore.gamesstoreapi.client.service;

import org.gamesstore.gamesstoreapi.client.dto.ClientRequestDTO;
import org.gamesstore.gamesstoreapi.client.model.Client;
import org.gamesstore.gamesstoreapi.client.repository.ClientRepository;
import org.gamesstore.gamesstoreapi.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import org.gamesstore.gamesstoreapi.client.dto.ClientResponseDTO;


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
        Client saved = clientRepository.save(client);
        return toResponseDTO(saved);
    }


    public List<ClientResponseDTO> listAllClient() {
        return clientRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }


    public ClientResponseDTO searchForIdClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com id " + id));
        return toResponseDTO(client);
    }


    public ClientResponseDTO updateClient(Long id, ClientRequestDTO dto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com id " + id));
        client.setName(dto.getName());
        client.setEmail(dto.getEmail());
        client.setTelefone(dto.getTelefone());
        if (dto.getSenha() != null && !dto.getSenha().isEmpty()) {
            client.setSenha(passwordEncoder.encode(dto.getSenha()));
        }
        Client updated = clientRepository.save(client);
        return toResponseDTO(updated);
    }


    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com id " + id));
        clientRepository.delete(client);
    }


    public List<ClientResponseDTO> searchForEmail(String email) {
        return clientRepository.findAll().stream()
                .filter(c -> c.getEmail().equalsIgnoreCase(email))
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }


    private ClientResponseDTO toResponseDTO(Client client) {
        ClientResponseDTO dto = new ClientResponseDTO();
        dto.setId(client.getId());
        dto.setName(client.getName());
        dto.setEmail(client.getEmail());
        dto.setTelefone(client.getTelefone());

        return dto;
    }


}