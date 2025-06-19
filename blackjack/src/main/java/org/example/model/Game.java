package org.example.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "games")
public class Game {

    @Id
    private int id;
    private String player_id;
    private Hand playerHand;
    private Hand croupierHand;
    private String result;
    private int bet;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(String player_id) {
        this.player_id = player_id;
    }

    public Hand getPlayerHand() {
        return playerHand;
    }

    public void setPlayerHand(Hand playerHand) {
        this.playerHand = playerHand;
    }

    public Hand getCroupierHand() {
        return croupierHand;
    }

    public void setCroupierHand(Hand croupierHand) {
        this.croupierHand = croupierHand;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getBet() {
        return bet;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }

}