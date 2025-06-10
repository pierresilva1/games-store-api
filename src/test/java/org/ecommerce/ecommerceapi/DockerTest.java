package org.ecommerce.ecommerceapi;Add commentMore actions

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

public class DockerTest {

    @Test
    void testDockerContainer() {
        try (PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")) {
            postgres.start();
            System.out.println("Container rodando: " + postgres.getJdbcUrl());
        }
    }
}