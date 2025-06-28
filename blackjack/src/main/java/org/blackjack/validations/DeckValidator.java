package org.blackjack.validations;

import org.blackjack.exceptions.DeckEmptyException;
import org.blackjack.model.Deck;
import org.blackjack.model.Game;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DeckValidator {

    public Mono<Void> validateDeck(Deck deck) {
        if (deck == null || deck.getCards().isEmpty()) {
            return Mono.error(new DeckEmptyException("Deck is empty or not initialized"));
        }
        return Mono.empty();
    }

}