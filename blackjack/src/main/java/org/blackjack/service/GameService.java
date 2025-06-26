package org.blackjack.service;

import org.blackjack.model.Card;
import org.blackjack.model.Deck;
import org.blackjack.model.Game;
import org.blackjack.model.Hand;
import org.blackjack.repositories.GameRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

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

    public Mono<Boolean> deleteGame(String id) {
        return gameRepository.findById(id)
                .flatMap(game -> gameRepository.deleteById(id).thenReturn(true))
                .defaultIfEmpty(false);
    }

    public Mono<Game> giveCardToPlayer(String gameId) {
        return gameRepository.findById(gameId)
                .flatMap(game -> {
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

    // playerStands()

    // TODO
    // ACABAR ESTO, ahora mismo ni filtra ni nada
    public Flux<Game> getRanking() {
        return gameRepository.findAll();
    }

}