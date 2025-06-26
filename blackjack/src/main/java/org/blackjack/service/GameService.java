package org.blackjack.service;

import org.blackjack.model.Game;
import org.blackjack.repositories.GameRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Mono<Game> addGame(Game game) {
        return gameRepository.save(game);
    }

    public Mono<Game> getGame(String id) {
        return gameRepository.findById(id);
    }

    public Mono<Game> updateGame(String id, Game game) {
        return gameRepository.findById(id)
                .flatMap(existingGame -> {
                    game.setId(id);
                    return gameRepository.save(game);
                });
    }

    public Mono<Boolean> deleteGame(String id) {
        return gameRepository.findById(id)
                .flatMap(game -> gameRepository.deleteById(id).thenReturn(true))
                .defaultIfEmpty(false);
    }

}