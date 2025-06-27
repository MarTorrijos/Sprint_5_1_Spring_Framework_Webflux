package org.blackjack.model;

import lombok.Getter;
import org.blackjack.model.enums.CardRank;
import org.blackjack.model.enums.CardSuit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class Deck {

    private List<Card> cards;

    public Deck() {
        reset();
    }

    public void reset() {
        cards = new ArrayList<>();
        for (CardSuit suit : CardSuit.values()) {
            for (CardRank rank : CardRank.values()) {
                cards.add(new Card(rank, suit));
            }
        }
        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card drawCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("No more cards in the deck");
        }
        return cards.removeFirst();
    }

}