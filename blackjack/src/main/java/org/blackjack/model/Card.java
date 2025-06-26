package org.blackjack.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.blackjack.model.enums.CardRank;
import org.blackjack.model.enums.CardSuit;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Card {

    private CardRank rank;
    private CardSuit suit;

}