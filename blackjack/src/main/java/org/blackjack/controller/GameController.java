package org.blackjack.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.blackjack.model.Game;
import org.blackjack.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/new")
    @Operation(summary = "Create a new game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Game created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
    })
    public Mono<ResponseEntity<Game>> createGame(@RequestBody Game game) {
        return gameService.createGame(game)
                .map(savedGame -> ResponseEntity.status(HttpStatus.CREATED).body(savedGame));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a game by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game found"),
            @ApiResponse(responseCode = "404", description = "No game found by this id")
    })
    public Mono<ResponseEntity<Game>> getGame(@PathVariable String id) {
        return gameService.getGame(id)
                .map(ResponseEntity::ok);
    }

    @GetMapping
    @Operation(summary = "Get all games")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All games"),
            @ApiResponse(responseCode = "204", description = "No games found")
    })
    public Flux<ResponseEntity<Game>> getAllGames() {
        return gameService.getAllGames()
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a game selected by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game deleted successfully"),
            @ApiResponse(responseCode = "404", description = "No game found by this id")
    })
    public Mono<ResponseEntity<Void>> deleteGame(@PathVariable String id) {
        return gameService.deleteGame(id)
                .map(deleted -> {
                    if (deleted) return ResponseEntity.ok().build();
                    throw new RuntimeException("Game not found with id: " + id);
                });
    }

}