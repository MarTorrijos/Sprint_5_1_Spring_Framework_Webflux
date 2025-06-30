package org.blackjack.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.blackjack.exceptions.GameFinishedException;
import org.blackjack.service.TurnService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "game")
public class TurnController {

    private final TurnService turnService;

    public TurnController(TurnService turnService) {
        this.turnService = turnService;
    }

    @PostMapping("/{id}/hit")
    @Operation(summary = "Deal a card to a player")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card dealt"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
    })
    public Mono<ResponseEntity<Object>> dealCardToPlayer(@PathVariable String id) {
        return turnService.dealCardToPlayer(id)
                .map(game -> ResponseEntity.ok((Object) game));
    }

    @PutMapping("/{id}/stand")
    @Operation(summary = "Player chooses to stand")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player stands"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
    })
    public Mono<ResponseEntity<Object>> playerStands(@PathVariable String id) {
        return turnService.playerStands(id)
                .map(message -> ResponseEntity.ok((Object) message));
    }

}