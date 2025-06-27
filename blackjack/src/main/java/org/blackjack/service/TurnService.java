package org.blackjack.service;

import org.blackjack.exceptions.GameFinishedException;
import org.blackjack.model.Card;
import org.blackjack.model.Deck;
import org.blackjack.model.Game;
import org.blackjack.model.enums.GameStatus;
import org.blackjack.repositories.GameRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class TurnService {

    private final GameRepository gameRepository;

    public TurnService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Mono<Game> dealCardToPlayer(String gameId) {
        return gameRepository.findById(gameId)
                .flatMap(this::validateGameStatus)
                .flatMap(this::initializeDeckIfNeeded)
                .flatMap(game -> drawCardFromDeck(game)
                        .flatMap(newCard -> updatePlayerHand(newCard, game)))
                .flatMap(gameRepository::save);
    }

    private Mono<Game> validateGameStatus(Game game) {
        if (game.getGameStatus() == GameStatus.FINISHED) {
            return Mono.error(new GameFinishedException("Game is already finished. No more cards can be dealt."));
        }
        return Mono.just(game);
    }

    private Mono<Card> drawCardFromDeck(Game game) {
        try {
            Card newCard = game.getDeck().drawCard();
            return Mono.just(newCard);
        } catch (IllegalStateException e) {
            return Mono.error(new RuntimeException("Deck is empty"));
        }
    }

    private Mono<Game> updatePlayerHand(Card newCard, Game game) {
        game.getPlayerHand().getCards().add(newCard);
        return Mono.just(game);
    }

    private Mono<Game> initializeDeckIfNeeded(Game game) {
        if (game.getDeck() == null) {
            game.setDeck(new Deck());
            game.getDeck().reset();
        }
        return Mono.just(game);
    }

    public Mono<Boolean> playerStands(String gameId) {
        return gameRepository.findById(gameId)
                .flatMap(game -> {
                    game.setGameStatus(GameStatus.FINISHED);
                    return gameRepository.save(game).map(savedGame -> true);
                })
                .defaultIfEmpty(false);
    }

}