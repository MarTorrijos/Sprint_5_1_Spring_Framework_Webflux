package org.blackjack.service;

import org.blackjack.model.Game;
import org.blackjack.repositories.GameRepository;
import org.bson.types.ObjectId;
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

    public Mono<Game> getGame(ObjectId id) {
        return gameRepository.findById(id);
    }

    public Mono<Game> updateGame(ObjectId id, Game game) {
        return gameRepository.findByObjectId(id)
                .flatMap(existingGame -> {
                    game.setId(id);
                    return gameRepository.save(game);
                });
    }

    public Mono<Boolean> deleteGame(ObjectId id) {
        return gameRepository.findByObjectId(id)
                .flatMap(game -> gameRepository.deleteByObjectId(id).thenReturn(true))
                .defaultIfEmpty(false);
    }

}