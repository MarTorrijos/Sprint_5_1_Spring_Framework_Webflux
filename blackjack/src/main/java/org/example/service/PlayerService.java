package org.example.service;

import org.example.model.Player;
import org.example.repositories.PlayerRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Mono<Player> addPlayer(Player player) {
        return playerRepository.save(player);
    }

    public Mono<Player> getPlayer(int id) {
        return playerRepository.findById(id);
    }

    public Mono<Player> updatePlayer(Player player) {
        return playerRepository.save(player);
    }

    public Mono<Void> deletePlayer(int id) {
        return playerRepository.deleteById(id);
    }

}