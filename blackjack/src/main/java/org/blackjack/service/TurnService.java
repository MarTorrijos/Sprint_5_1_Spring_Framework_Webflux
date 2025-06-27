package org.blackjack.service;

import org.blackjack.model.Card;
import org.blackjack.model.Deck;
import org.blackjack.model.Game;
import org.blackjack.model.enums.GameStatus;
import org.blackjack.repositories.GameRepository;
import org.blackjack.validations.DeckValidator;
import org.blackjack.validations.GameValidator;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class TurnService {

    private final GameRepository gameRepository;
    private final GameValidator gameValidator;
    private final DeckValidator deckValidator;

    public TurnService(GameRepository gameRepository, GameValidator gameValidator, DeckValidator deckValidator) {
        this.gameRepository = gameRepository;
        this.gameValidator = gameValidator;
        this.deckValidator = deckValidator;
    }

    public Mono<Game> dealCardToPlayer(String gameId) {
        return gameRepository.findById(gameId)
                .flatMap(gameValidator::validateGameStatus)
                .flatMap(this::initializeDeckIfNeeded)
                .flatMap(game -> drawCardFromDeck(game)
                        .flatMap(newCard -> updatePlayerHand(newCard, game)))
                .flatMap(gameRepository::save);
    }

    private Mono<Card> drawCardFromDeck(Game game) {
        return deckValidator.validateDeck(game.getDeck())
                .then(Mono.fromCallable(() -> game.getDeck().drawCard()));
    }

    private Mono<Game> updatePlayerHand(Card newCard, Game game) {
        game.getPlayerHand().getCards().add(newCard);
        return Mono.just(game);
    }

    public Mono<Game> initializeDeckIfNeeded(Game game) {
        if (game.getDeck() == null) {
            game.setDeck(new Deck());
            game.getDeck().reset();
        }
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

}