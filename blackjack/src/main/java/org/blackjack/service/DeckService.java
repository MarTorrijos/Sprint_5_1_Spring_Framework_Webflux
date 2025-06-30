package org.blackjack.service;

import org.blackjack.exceptions.CardNullException;
import org.blackjack.model.Card;
import org.blackjack.model.Deck;
import org.blackjack.model.Game;
import org.blackjack.validations.DeckValidator;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DeckService {
    private final DeckValidator deckValidator;

    public DeckService(DeckValidator deckValidator) {
        this.deckValidator = deckValidator;
    }

    public Mono<Game> initializeDeck(Game game) {
        if (game.getDeck() == null) {
            game.setDeck(new Deck());
            game.getDeck().reset();
        }
        return Mono.just(game);
    }

    public Mono<Card> drawCard(Game game) {
        return deckValidator.validateDeck(game.getDeck())
                .then(Mono.fromCallable(() -> {
                    Card card = game.getDeck().drawCard();
                    if (card == null) throw new CardNullException("Card draw returned null");
                    return card;
                }));
    }

}