package org.blackjack.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.blackjack.model.Player;
import org.blackjack.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/player")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping
    @Operation(summary = "Create a new player")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Player created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
    })
    public Mono<ResponseEntity<Player>> addPlayer(@RequestBody Player player) {
        return playerService.addPlayer(player)
                .map(savedPlayer -> ResponseEntity.status(HttpStatus.CREATED).body(savedPlayer));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a player by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player found"),
            @ApiResponse(responseCode = "204", description = "No player found by this id")
    })
    public Mono<ResponseEntity<Player>> getPlayer(@PathVariable String id) {
        return playerService.getPlayer(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a player selected by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player updated successfully"),
            @ApiResponse(responseCode = "204", description = "No player found by this id")
    })
    public Mono<ResponseEntity<Player>> updatePlayer(@PathVariable String id, @RequestBody Player player) {
        return playerService.updatePlayer(id, player)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a player selected by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player deleted successfully"),
            @ApiResponse(responseCode = "204", description = "No player found by this id")
    })
    public Mono<ResponseEntity<Void>> deletePlayer(@PathVariable String id) {
        return playerService.deletePlayer(id)
                .map(deleted -> deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build());
    }

    @GetMapping("/ranking")
    @Operation(summary = "Show the ranking of games won")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Showing rankin")
    })
    public Flux<ResponseEntity<Player>> getRanking() {
        return playerService.getRanking()
                .map(ResponseEntity::ok);
    }

}