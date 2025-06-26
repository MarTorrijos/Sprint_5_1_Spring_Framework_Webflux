package org.blackjack.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.blackjack.model.Game;
import org.blackjack.service.TurnService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "game")
public class TurnController {

    private final TurnService turnService;

    public TurnController(TurnService turnService) {
        this.turnService = turnService;
    }

    @Operation(summary = "Deal a card to a player")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Card dealt"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
    })
    @PostMapping("/{id}/hit")
    public Mono<ResponseEntity<Game>> dealCardToPlayer(@PathVariable String id) {
        return turnService.dealCardToPlayer(id)
                .map(ResponseEntity::ok)
                .onErrorResume(IllegalStateException.class, e ->
                        Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build())
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/stand")
    public Mono<ResponseEntity<Void>> playerStands(@PathVariable String id) {
        return turnService.playerStands(id)
                .map(updated -> updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build());
    }

}