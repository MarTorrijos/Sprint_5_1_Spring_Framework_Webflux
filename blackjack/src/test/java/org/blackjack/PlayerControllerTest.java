package org.blackjack;

import org.blackjack.controller.PlayerController;
import org.blackjack.exceptions.PlayerNotFoundException;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlayerControllerTest {

    @Mock private PlayerService playerService;

    @InjectMocks private PlayerController playerController;
    private Player testPlayer;

    @BeforeEach
    void setUp() {
        testPlayer = new Player("1", "Player", 100);
    }

    @Test
    void addPlayerTest() {
        when(playerService.addPlayer(testPlayer)).thenReturn(Mono.just(testPlayer));

        Mono<ResponseEntity<Player>> responseMono = playerController.addPlayer(testPlayer);
        ResponseEntity<Player> response = responseMono.block();

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testPlayer, response.getBody());

        verify(playerService).addPlayer(testPlayer);
    }

    @Test
    void getPlayerTest() {
        when(playerService.getPlayer("1")).thenReturn(Mono.just(testPlayer));

        Mono<ResponseEntity<Player>> responseMono = playerController.getPlayer("1");
        ResponseEntity<Player> response = responseMono.block();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testPlayer, response.getBody());

        verify(playerService).getPlayer("1");
    }

    @Test
    void getPlayerNotFoundTest() {
        when(playerService.getPlayer("999")).thenReturn(Mono.error(
                new PlayerNotFoundException("Player not found with id: 999")));

        PlayerNotFoundException thrown = assertThrows(PlayerNotFoundException.class,
                () -> playerController.getPlayer("999").block());

        assertEquals("Player not found with id: 999", thrown.getMessage());
        verify(playerService).getPlayer("999");
    }

    @Test
    void updatePlayerTest() {
        when(playerService.updatePlayer("1", testPlayer)).thenReturn(Mono.just(testPlayer));

        Mono<ResponseEntity<Player>> responseMono = playerController.updatePlayer("1", testPlayer);
        ResponseEntity<Player> response = responseMono.block();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testPlayer, response.getBody());

        verify(playerService).updatePlayer("1", testPlayer);
    }

    @Test
    void deletePlayerTest() {
        when(playerService.deletePlayer("1")).thenReturn(Mono.just(true));

        Mono<ResponseEntity<Void>> responseMono = playerController.deletePlayer("1");
        ResponseEntity<Void> response = responseMono.block();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(playerService).deletePlayer("1");
    }

    @Test
    void deletePlayerNotFoundTest() {
        when(playerService.deletePlayer("999")).thenReturn(Mono.error(
                new PlayerNotFoundException("Player not found with id: 999")));

        PlayerNotFoundException ex = assertThrows(PlayerNotFoundException.class,
                () -> playerController.deletePlayer("999").block());

        assertEquals("Player not found with id: 999", ex.getMessage());
        verify(playerService).deletePlayer("999");
    }

}