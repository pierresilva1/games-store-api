package org.gamesstore.gamesstoreapi.order.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.gamesstore.gamesstoreapi.client.model.Client;
import org.gamesstore.gamesstoreapi.product.model.Product;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "pedido")
public class Pedido {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDateTime dateCreate;

    @Setter
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Client client;

    @ManyToMany
    @JoinTable(
            name = "pedido_produto",
            joinColumns = @JoinColumn(name = "pedido_id"),
            inverseJoinColumns = @JoinColumn(name = "produto_id")
    )
    private List<Product> products = new ArrayList<>();


    public Pedido() {
    }

    public Pedido(LocalDateTime dateCreate, OrderStatus status, Client client, List<Product> products) {
        this.dateCreate = dateCreate;
        this.status = status;
        this.client = client;
        this.products = products;
    }

    // Getters e Setters

    public void setCreatedAt(LocalDateTime dateCreate) {
        this.dateCreate = dateCreate;
    }

    public void setClients(Client client) {
        this.client = client;
    }

    public void prosecutor(List<Product> products) {
        this.products = products;
    }





}
