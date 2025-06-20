package org.blackjack.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.blackjack.model.enums.Suit;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Card {

    private int rank;
    private Suit suit;

}