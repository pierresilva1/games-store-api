package org.gamesstore.gamesstoreapi.client.repository;

import org.gamesstore.gamesstoreapi.client.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findByEmailContainingIgnoreCase(String email);
}
