package org.blackjack.service;

import org.blackjack.model.Card;
import org.blackjack.model.Game;
import org.blackjack.model.enums.GameStatus;
import org.blackjack.repositories.GameRepository;
import org.blackjack.validations.GameValidator;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class TurnService {

    private final GameRepository gameRepository;
    private final GameValidator gameValidator;
    private final DeckService deckService;

    public TurnService(GameRepository gameRepository, GameValidator gameValidator, DeckService deckService) {
        this.gameRepository = gameRepository;
        this.gameValidator = gameValidator;
        this.deckService = deckService;
    }

    public Mono<Game> dealCardToPlayer(String gameId) {
        return gameRepository.findById(gameId)
                .flatMap(gameValidator::validateGameStatus)
                .flatMap(deckService::initializeDeck)
                .flatMap(game -> deckService.drawCard(game)
                        .flatMap(newCard -> updatePlayerHand(newCard, game)))
                .flatMap(this::isGameFinished)
                .flatMap(gameRepository::save);
    }

    public Mono<Game> dealTwoCardsToPlayer(String gameId) {
        return gameRepository.findById(gameId)
                .flatMap(gameValidator::validateGameStatus)
                .flatMap(deckService::initializeDeck)
                .flatMap(game -> deckService.drawCard(game)
                        .flatMap(firstCard -> updatePlayerHand(firstCard, game))
                        .flatMap(updatedGame -> deckService.drawCard(updatedGame)
                                .flatMap(secondCard -> updatePlayerHand(secondCard, updatedGame))))
                .flatMap(gameRepository::save);
    }

    public Mono<Game> dealTwoCardsToCroupier(String gameId) {
        return gameRepository.findById(gameId)
                .flatMap(gameValidator::validateGameStatus)
                .flatMap(deckService::initializeDeck)
                .flatMap(game -> deckService.drawCard(game)
                        .flatMap(firstCard -> updateCroupierHand(firstCard, game))
                        .flatMap(updatedGame -> deckService.drawCard(updatedGame)
                                .flatMap(secondCard -> updateCroupierHand(secondCard, updatedGame))))
                .flatMap(gameRepository::save);
    }

    private Mono<Game> updatePlayerHand(Card newCard, Game game) {
        game.getPlayerHand().getCards().add(newCard);
        game.getPlayerHand().setValue();
        return Mono.just(game);
    }

    private Mono<Game> updateCroupierHand(Card newCard, Game game) {
        game.getCroupierHand().getCards().add(newCard);
        game.getCroupierHand().setValue();
        return Mono.just(game);
    }

    public Mono<String> playerStands(String gameId) {
        return gameRepository.findById(gameId)
                .flatMap(game -> {
                    game.setGameStatus(GameStatus.FINISHED);
                    return gameRepository.save(game)
                            .then(Mono.just("Player has stood. Game is now finished."));
                })
                .defaultIfEmpty("Game not found or already finished.");
    }

    public Mono<Game> isGameFinished(Game game) {
        if (game.getPlayerHand().getValue() > 21 || game.getCroupierHand().getValue() > 21) {
            game.setGameStatus(GameStatus.FINISHED);
        }
        return gameRepository.save(game);
    }

}