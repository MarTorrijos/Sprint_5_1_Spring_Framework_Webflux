package org.blackjack.controller;

import org.blackjack.model.Player;
import org.blackjack.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/player")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping("")
    public Mono<ResponseEntity<Player>> addPlayer(@RequestBody Player player) {
        return playerService.addPlayer(player)
                .map(savedPlayer -> ResponseEntity.status(HttpStatus.CREATED).body(savedPlayer));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Player>> getPlayer(@PathVariable int id) {
        return playerService.getPlayer(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Player>> updatePlayer(@PathVariable int id, @RequestBody Player player) {
        return playerService.getPlayer(id)
                .flatMap(existingPlayer -> {
                    player.setId(id);
                    return playerService.updatePlayer(player)
                            .map(ResponseEntity::ok);
                })
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePlayer(@PathVariable int id) {
        return playerService.getPlayer(id)
                .flatMap(existingPlayer ->
                        playerService.deletePlayer(id)
                                .then(Mono.just(ResponseEntity.ok().<Void>build()))
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}