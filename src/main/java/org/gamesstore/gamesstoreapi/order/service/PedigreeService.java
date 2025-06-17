package org.gamesstore.gamesstoreapi.order.service;

import org.gamesstore.gamesstoreapi.order.model.Pedido;
import org.gamesstore.gamesstoreapi.order.repository.PedigreeRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PedigreeService {

    private final PedigreeRepository pedigreeRepository;

    public PedigreeService(PedigreeRepository pedigreeRepository) {
        this.pedigreeRepository = pedigreeRepository;
    }

    public List<Pedido> listarTodos() {
        return pedigreeRepository.findAll();
    }

    public Optional<Pedido> buscarPorId(Long id) {
        return pedigreeRepository.findById(id);
    }

    public Pedido salvar(Pedido pedido) {
        return pedigreeRepository.save(pedido);
    }

    public void deletar(Long id) {
        pedigreeRepository.deleteById(id);
    }
}