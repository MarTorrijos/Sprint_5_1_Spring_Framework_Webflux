package org.blackjack.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.blackjack.model.Game;
import org.blackjack.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @Operation(summary = "Create a new game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Game created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
    })
    @PostMapping
    public Mono<ResponseEntity<Game>> addGame(@RequestBody Game game) {
        return gameService.addGame(game)
                .map(savedGame -> ResponseEntity.status(HttpStatus.CREATED).body(savedGame));
    }

    @Operation(summary = "Get a game by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game found"),
            @ApiResponse(responseCode = "204", description = "No game found by this id")
    })
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Game>> getGame(@PathVariable String id) {
        return gameService.getGame(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update a game selected by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game updated successfully"),
            @ApiResponse(responseCode = "204", description = "No game found by this id")
    })
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Game>> updateGame(@PathVariable String id, @RequestBody Game game) {
        return gameService.updateGame(id, game)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @Operation(summary = "Delete a game selected by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game deleted successfully"),
            @ApiResponse(responseCode = "204", description = "No game found by this id")
    })
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Game>> deleteGame(@PathVariable String id) {
        return gameService.deleteGame(id)
                .map(deleted -> deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build());
    }

}