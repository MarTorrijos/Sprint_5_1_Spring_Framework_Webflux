package org.blackjack.validations;

import org.blackjack.exceptions.GameFinishedException;
import org.blackjack.model.Game;
import org.blackjack.model.enums.GameStatus;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Component;

@Component
public class GameValidator {

    public Mono<Game> validateGameStatus(Game game) {
        if (game.getGameStatus() == GameStatus.FINISHED) {
            return Mono.error(new GameFinishedException("Game is already finished. No more cards can be dealt"));
        }
        return Mono.just(game);
    }

}