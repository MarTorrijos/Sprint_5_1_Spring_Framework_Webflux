package org.blackjack.service;

import org.blackjack.exceptions.GameNotFoundException;
import org.blackjack.model.Game;
import org.blackjack.model.enums.GameStatus;
import org.blackjack.repositories.GameRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final TurnService turnService;
    private final HandService handService;
    private final DeckService deckService;

    public GameService(GameRepository gameRepository, TurnService turnService, HandService handService,
                       DeckService deckService) {
        this.gameRepository = gameRepository;
        this.turnService = turnService;
        this.handService = handService;
        this.deckService = deckService;
    }

    public Mono<Game> createGame(Game game) {
        deckService.initializeDeck(game);
        handService.initializeHands(game);
        game.setGameStatus(GameStatus.PLAYING);

        return Mono.just(game)
                .flatMap(gameRepository::save)
                .flatMap(savedGame -> turnService.dealTwoCards(savedGame.getId()).thenReturn(savedGame));
    }

    public Mono<Game> getGame(String id) {
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new GameNotFoundException("Game not found with id: " + id)));
    }

    public Flux<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public Mono<Boolean> deleteGame(String id) {
        return gameRepository.findById(id)
                .flatMap(game -> gameRepository.deleteById(id).thenReturn(true))
                .switchIfEmpty(Mono.error(new GameNotFoundException("Game not found with id: " + id)));
    }

}