package org.blackjack.service;

import org.blackjack.exceptions.GameFinishedException;
import org.blackjack.exceptions.GameNotFoundException;
import org.blackjack.model.Card;
import org.blackjack.model.Game;
import org.blackjack.model.Player;
import org.blackjack.model.enums.GameStatus;
import org.blackjack.repositories.GameRepository;
import org.blackjack.repositories.PlayerRepository;
import org.blackjack.validations.GameValidator;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class TurnService {
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final GameValidator gameValidator;
    private final DeckService deckService;

    public TurnService(GameRepository gameRepository, PlayerRepository playerRepository,
                       GameValidator gameValidator, DeckService deckService) {
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
        this.gameValidator = gameValidator;
        this.deckService = deckService;
    }

    public Mono<Game> dealCardToPlayer(String gameId) {
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new GameNotFoundException("Game not found with id: " + gameId)))
                .flatMap(gameValidator::validateGameStatus)
                .flatMap(game -> {
                    if (game.getGameStatus() == GameStatus.FINISHED) {
                        throw new GameFinishedException("Cannot deal card. Game already finished.");
                    }
                    return drawCardAndUpdateHand(game, true);
                })
                .flatMap(this::checkIfGameFinished)
                .flatMap(gameRepository::save);
    }

    public Mono<Game> dealTwoCards(String gameId) {
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new GameNotFoundException("Game not found with id: " + gameId)))
                .flatMap(gameValidator::validateGameStatus)
                .flatMap(game -> drawTwoCardsAndUpdateHand(game, true)
                        .flatMap(updated -> drawTwoCardsAndUpdateHand(updated, false)))
                .flatMap(gameRepository::save);
    }

    private Mono<Game> drawCardAndUpdateHand(Game game, boolean isPlayer) {
        return deckService.drawCard(game)
                .flatMap(card -> updateHand(card, game, isPlayer));
    }

    private Mono<Game> drawTwoCardsAndUpdateHand(Game game, boolean isPlayer) {
        return drawCardAndUpdateHand(game, isPlayer)
                .flatMap(updatedGame -> drawCardAndUpdateHand(updatedGame, isPlayer));
    }

    private Mono<Game> updateHand(Card card, Game game, boolean isPlayer) {
        if (isPlayer) {
            game.getPlayerHand().getCards().add(card);
            game.getPlayerHand().setValue();
        } else {
            game.getCroupierHand().getCards().add(card);
            game.getCroupierHand().setValue();
        }
        return Mono.just(game);
    }

    public Mono<String> playerStands(String gameId) {
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new GameNotFoundException("Game not found with id: " + gameId)))
                .flatMap(game -> finishGame(game)
                        .flatMap(updated -> playerRepository.findById(String.valueOf(updated.getPlayer_id()))
                                .flatMap(player -> calculateWinner(updated, player))
                                .then(Mono.just("Player has stood. Game is now finished"))));
    }

    private Mono<Game> finishGame(Game game) {
        game.setGameStatus(GameStatus.FINISHED);
        return gameRepository.save(game);
    }

    private Mono<Game> calculateWinner(Game game, Player player) {
        if (game.getPlayerHand().getValue() > 21) {
            game.setPlayerWon(false);
        } else {
            int playerValue = game.getPlayerHand().getValue();
            int croupierValue = game.getCroupierHand().getValue();
            if (playerValue > croupierValue) {
                game.setPlayerWon(true);
                player.setGamesWon(player.getGamesWon() + 1);
                return playerRepository.save(player).then(gameRepository.save(game));
            } else {
                game.setPlayerWon(false);
            }
        }
        return gameRepository.save(game);
    }

    private Mono<Game> checkIfGameFinished(Game game) {
        if (isGameOver(game)) {
            game.setGameStatus(GameStatus.FINISHED);
        }
        return gameRepository.save(game);
    }

    private boolean isGameOver(Game game) {
        return game.getPlayerHand().getValue() > 21 || game.getCroupierHand().getValue() > 21;
    }

}