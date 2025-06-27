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

    @Operation(summary = "Deal a card to a player")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Card dealt"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
    })
    @PostMapping("/{id}/hit")
    public Mono<ResponseEntity<Object>> dealCardToPlayer(@PathVariable String id) {
        return turnService.dealCardToPlayer(id)
                .map(game -> ResponseEntity.ok((Object) game))
                .onErrorResume(GameFinishedException.class, e -> {
                    Map<String, String> errorResponse = new HashMap<>();
                    errorResponse.put("message", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse));
                })
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/stand")
    public Mono<ResponseEntity<Object>> playerStands(@PathVariable String id) {
        return turnService.playerStands(id)
                .map(message -> ResponseEntity.ok((Object) message))
                .onErrorResume(GameFinishedException.class, e -> {
                    Map<String, String> errorResponse = new HashMap<>();
                    errorResponse.put("message", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse));
                })
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}