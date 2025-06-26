package org.blackjack.service;

import org.blackjack.model.Card;
import org.blackjack.model.Deck;
import org.blackjack.model.Game;
import org.blackjack.model.Hand;
import org.blackjack.model.enums.GameStatus;
import org.blackjack.repositories.GameRepository;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

public class TurnService {

    private final GameRepository gameRepository;

    public TurnService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Mono<Game> dealCardToPlayer(String gameId) {
        return gameRepository.findById(gameId)
                .flatMap(game -> {
                    if (game.getGameStatus() == GameStatus.FINISHED) {
                        return Mono.error(new IllegalStateException("Cannot deal card, game is finished"));
                    }

                    if (game.getDeck() == null) {
                        Deck deck = new Deck();
                        game.setDeck(deck);
                    }

                    Card newCard;
                    try {
                        newCard = game.getDeck().drawCard();
                    } catch (IllegalStateException e) {
                        return Mono.error(new RuntimeException("Deck is empty"));
                    }

                    if (game.getPlayerHand() == null) {
                        game.setPlayerHand(new Hand());
                    }

                    if (game.getPlayerHand().getCards() == null) {
                        game.getPlayerHand().setCards(new ArrayList<>());
                    }

                    game.getPlayerHand().getCards().add(newCard);

                    return gameRepository.save(game);
                });
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