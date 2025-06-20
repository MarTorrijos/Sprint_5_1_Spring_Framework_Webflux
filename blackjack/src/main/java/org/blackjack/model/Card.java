package org.blackjack.model;

import org.blackjack.model.enums.Suit;

public class Card {

    private int rank;
    private Suit suit;

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public void setSuit(Suit suit) {
        this.suit = suit;
    }

}