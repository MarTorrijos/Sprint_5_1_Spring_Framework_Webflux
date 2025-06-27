package org.blackjack.validations;

import org.blackjack.exceptions.CardNullException;
import org.blackjack.exceptions.DeckEmptyException;
import org.blackjack.model.Card;
import org.blackjack.model.Deck;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class DeckValidator {

    public Mono<Void> validateDeck(Deck deck) {
        if (deck == null || deck.getCards().isEmpty()) {
            return Mono.error(new DeckEmptyException("Deck is empty or not initialized"));
        }
        return Mono.empty();
    }

    public Mono<Void> validateCard(Card card) {
        if (card == null) {
            return Mono.error(new CardNullException("Card is null"));
        }
        return Mono.empty();
    }

}