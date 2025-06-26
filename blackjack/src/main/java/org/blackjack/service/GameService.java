package org.blackjack.service;

import org.blackjack.model.Game;
import org.blackjack.repositories.GameRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Mono<Game> createGame(Game game) {
        return gameRepository.save(game);
    }

    public Mono<Game> getGame(String id) {
        return gameRepository.findById(id);
    }

    public Flux<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public Mono<Boolean> deleteGame(String id) {
        return gameRepository.findById(id)
                .flatMap(game -> gameRepository.deleteById(id).thenReturn(true))
                .defaultIfEmpty(false);
    }

    // TODO
    // ACABAR ESTO, ahora mismo ni filtra ni nada
    public Flux<Game> getRanking() {
        return gameRepository.findAll();
    }

}