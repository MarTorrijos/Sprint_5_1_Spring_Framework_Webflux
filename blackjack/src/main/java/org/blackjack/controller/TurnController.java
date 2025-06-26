package org.blackjack.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.blackjack.model.Game;
import org.blackjack.service.TurnService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // TODO
    // playerStands()

}