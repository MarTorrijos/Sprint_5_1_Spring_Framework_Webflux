package org.blackjack.service;

import org.blackjack.model.Game;
import org.blackjack.model.Hand;
import org.springframework.stereotype.Service;

@Service
public class HandService {

    public void initializeHands(Game game) {
        initializePlayerHand(game);
        initializeCroupierHand(game);
    }

    public void initializePlayerHand(Game game) {
        if (game.getPlayerHand() == null) {
            game.setPlayerHand(new Hand());
        }
    }

    public void initializeCroupierHand(Game game) {
        if (game.getCroupierHand() == null) {
            game.setCroupierHand(new Hand());
        }
    }

}