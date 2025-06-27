package org.blackjack.service;

import org.blackjack.model.Player;
import org.blackjack.repositories.PlayerRepository;
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

    public Mono<Player> getPlayer(String id) {
        return playerRepository.findById(id);
    }

    public Mono<Player> updatePlayer(String id, Player player) {
        return playerRepository.findById(id)
                .flatMap(existingPlayer -> {
                    player.setId(id);
                    return playerRepository.save(player);
                });
    }

    public Mono<Boolean> deletePlayer(String id) {
        return playerRepository.findById(id)
                .flatMap(player -> playerRepository.deleteById(id).thenReturn(true))
                .defaultIfEmpty(false);
    }

}