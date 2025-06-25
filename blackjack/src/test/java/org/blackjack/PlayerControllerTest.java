package org.blackjack;

import org.blackjack.controller.PlayerController;
import org.blackjack.model.Player;
import org.blackjack.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlayerControllerTest {

    @Mock
    private PlayerService playerService;

    @InjectMocks
    private PlayerController playerController;
    private Player testPlayer;

    @BeforeEach
    void setUp() {
        testPlayer = new Player(1, "Player", 100);
    }

    @Test
    void addPlayerTest() {
        when(playerService.addPlayer(testPlayer)).thenReturn(Mono.just(testPlayer));

        Mono<ResponseEntity<Player>> responseMono = playerController.addPlayer(testPlayer);

        ResponseEntity<Player> response = responseMono.block();
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testPlayer, response.getBody());

        verify(playerService, times(1)).addPlayer(testPlayer);
    }

    @Test
    void getPlayerTest() {
        when(playerService.getPlayer(testPlayer.getId())).thenReturn(Mono.just(testPlayer));

        Mono<ResponseEntity<Player>> responseMono = playerController.getPlayer(testPlayer.getId());

        ResponseEntity<Player> response = responseMono.block();
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testPlayer, response.getBody());

        verify(playerService, times(1)).getPlayer(testPlayer.getId());
    }

    @Test
    void updatePlayer() {
        int playerId = testPlayer.getId();

        when(playerService.updatePlayer(playerId, testPlayer)).thenReturn(Mono.just(testPlayer));

        Mono<ResponseEntity<Player>> responseMono = playerController.updatePlayer(playerId, testPlayer);

        ResponseEntity<Player> response = responseMono.block();
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testPlayer, response.getBody());

        verify(playerService, times(1)).updatePlayer(playerId, testPlayer);
    }

    @Test
    void deletePlayer() {
        int playerId = testPlayer.getId();

        when(playerService.deletePlayer(playerId)).thenReturn(Mono.just(true));

        Mono<ResponseEntity<Void>> responseMono = playerController.deletePlayer(playerId);

        ResponseEntity<Void> response = responseMono.block();
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());

        verify(playerService, times(1)).deletePlayer(playerId);
    }

}