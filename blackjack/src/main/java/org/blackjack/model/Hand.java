package org.blackjack.model;

import org.blackjack.model.enums.CardRank;

import java.util.ArrayList;
import java.util.List;

public class Hand {

    private List<Card> cards = new ArrayList<>();
    private int value;

    public Hand() { }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
        setValue();
    }

    public int getValue() {
        return value;
    }

    public void setValue() {
        int totalValue = 0;
        int aceCount = 0;

        for (Card card : cards) {
            totalValue += card.getRank().getValue();
            if (card.getRank() == CardRank.ACE) {
                aceCount++;
            }
        }

        while (totalValue > 21 && aceCount > 0) {
            totalValue -= 10;
            aceCount--;
        }

        this.value = totalValue;
    }

}