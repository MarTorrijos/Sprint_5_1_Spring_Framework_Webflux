package org.example.controller;

import org.example.model.Player;
import org.example.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/player")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @PostMapping("/add")
    public Mono<ResponseEntity<Player>> addPlayer(@RequestBody Player player) {
        return playerService.addPlayer(player)
                .map(savedPlayer -> ResponseEntity.status(HttpStatus.CREATED).body(savedPlayer));
    }

    @GetMapping("/get/{id}")
    public Mono<ResponseEntity<Player>> getPlayer(@PathVariable int id) {
        return playerService.getPlayer(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{id}")
    public Mono<ResponseEntity<Player>> updatePlayer(@PathVariable int id, @RequestBody Player player) {
        return playerService.getPlayer(id)
                .flatMap(existingPlayer -> {
                    player.setId(id);
                    return playerService.updatePlayer(player)
                            .map(ResponseEntity::ok);
                })
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<Void>> deletePlayer(@PathVariable int id) {
        return playerService.getPlayer(id)
                .flatMap(existingPlayer ->
                        playerService.deletePlayer(id)
                                .then(Mono.just(ResponseEntity.ok().<Void>build()))
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}