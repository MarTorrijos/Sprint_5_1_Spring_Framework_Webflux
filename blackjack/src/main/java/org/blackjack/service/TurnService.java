package org.blackjack.service;

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

    public TurnService(GameRepository gameRepository, PlayerRepository playerRepository, GameValidator gameValidator,
                       DeckService deckService) {
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;
        this.gameValidator = gameValidator;
        this.deckService = deckService;
    }

    public Mono<Game> dealCardToPlayer(String gameId) {
        return gameRepository.findById(gameId)
                .flatMap(gameValidator::validateGameStatus)
                .flatMap(game -> drawCardAndUpdateHand(game, true))
                .flatMap(this::checkIfGameFinished)
                .flatMap(gameRepository::save);
    }

    public Mono<Game> dealTwoCards(String gameId) {
        return gameRepository.findById(gameId)
                .flatMap(gameValidator::validateGameStatus)
                .flatMap(game -> drawTwoCardsAndUpdateHand(game, true))
                .flatMap(game -> drawTwoCardsAndUpdateHand(game, false))
                .flatMap(gameRepository::save);
    }

    private Mono<Game> drawCardAndUpdateHand(Game game, boolean isPlayer) {
        return deckService.drawCard(game)
                .flatMap(newCard -> updateHand(newCard, game, isPlayer));
    }

    private Mono<Game> drawTwoCardsAndUpdateHand(Game game, boolean isPlayer) {
        return drawCardAndUpdateHand(game, isPlayer)
                .flatMap(updatedGame -> drawCardAndUpdateHand(updatedGame, isPlayer));
    }

    private Mono<Game> updateHand(Card newCard, Game game, boolean isPlayer) {
        if (isPlayer) {
            game.getPlayerHand().getCards().add(newCard);
            game.getPlayerHand().setValue();
        } else {
            game.getCroupierHand().getCards().add(newCard);
            game.getCroupierHand().setValue();
        }
        return Mono.just(game);
    }

    public Mono<String> playerStands(String gameId) {
        return gameRepository.findById(gameId)
                .flatMap(game -> finishGame(game)
                        .flatMap(updatedGame -> {
                            return playerRepository.findById(String.valueOf(updatedGame.getPlayer_id()))
                                    .flatMap(player -> calculateWinner(updatedGame, player));
                        })
                        .then(Mono.just("Player has stood. Game is now finished")));
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
                return playerRepository.save(player)
                        .then(gameRepository.save(game));
            } else if (playerValue < croupierValue) {
                game.setPlayerWon(false);
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