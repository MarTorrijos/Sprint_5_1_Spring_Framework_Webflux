package org.blackjack.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Document(collection = "games")
public class Game {

    @Id
    private String id;
    private String player_id;
    private Hand playerHand;
    private Hand croupierHand;

    @JsonIgnore
    private Deck deck;

    private String result;
    private int bet;

}