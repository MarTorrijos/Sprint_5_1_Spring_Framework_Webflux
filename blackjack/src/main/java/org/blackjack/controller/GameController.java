package org.blackjack.controller;

import org.blackjack.model.Game;
import org.blackjack.service.GameService;
import org.bson.types.ObjectId;
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

    @PostMapping("")
    public Mono<ResponseEntity<Game>> addGame(@RequestBody Game game) {
        return gameService.addGame(game)
                .map(savedGame -> ResponseEntity.status(HttpStatus.CREATED).body(savedGame));
    }

    @GetMapping("/{id")
    public Mono<ResponseEntity<Game>> getGame(@PathVariable ObjectId id) {
        return gameService.getGame(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Game>> updateGame(@PathVariable ObjectId id, @RequestBody Game game) {
        return gameService.updateGame(id, game)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Game>> deleteGame(@PathVariable ObjectId id) {
        return gameService.deleteGame(id)
                .map(deleted -> deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build());
    }

}