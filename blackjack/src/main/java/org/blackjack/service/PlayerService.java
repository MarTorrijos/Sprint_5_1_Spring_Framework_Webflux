package org.blackjack.service;

import org.blackjack.exceptions.PlayerNotFoundException;
import org.blackjack.model.Player;
import org.blackjack.repositories.PlayerRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;

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
        return playerRepository.findById(id)
                .switchIfEmpty(Mono.error(new PlayerNotFoundException("Player not found with id: " + id)));
    }

    public Mono<Player> updatePlayer(String id, Player player) {
        return playerRepository.findById(id)
                .switchIfEmpty(Mono.error(new PlayerNotFoundException("Player not found with id: " + id)))
                .flatMap(existingPlayer -> {
                    player.setId(id);
                    return playerRepository.save(player);
                });
    }

    public Mono<Boolean> deletePlayer(String id) {
        return playerRepository.findById(id)
                .flatMap(player -> playerRepository.deleteById(id).thenReturn(true))
                .switchIfEmpty(Mono.error(new PlayerNotFoundException("Player not found with id: " + id)));
    }

    public Flux<Player> getRanking() {
        return playerRepository.findAll()
                .sort(Comparator.comparingInt(Player::getGamesWon).reversed()
                        .thenComparing(Player::getName));
    }

}