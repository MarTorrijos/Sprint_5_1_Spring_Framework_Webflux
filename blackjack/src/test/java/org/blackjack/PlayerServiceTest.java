package org.blackjack;

import org.blackjack.exceptions.PlayerNotFoundException;
import org.blackjack.model.Player;
import org.blackjack.repositories.PlayerRepository;
import org.blackjack.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock private PlayerRepository playerRepository;
    @InjectMocks private PlayerService playerService;

    private Player testPlayer;

    @BeforeEach
    void setUp() {
        testPlayer = new Player("1", "Player", 100);
    }

    @Test
    void testAddPlayer() {
        when(playerRepository.save(testPlayer)).thenReturn(Mono.just(testPlayer));

        StepVerifier.create(playerService.addPlayer(testPlayer))
                .expectNext(testPlayer)
                .verifyComplete();

        verify(playerRepository).save(testPlayer);
    }

    @Test
    void testGetPlayer() {
        when(playerRepository.findById("1")).thenReturn(Mono.just(testPlayer));

        StepVerifier.create(playerService.getPlayer("1"))
                .expectNext(testPlayer)
                .verifyComplete();

        verify(playerRepository).findById("1");
    }

    @Test
    void testGetPlayerNotFound() {
        when(playerRepository.findById("999")).thenReturn(Mono.empty());

        StepVerifier.create(playerService.getPlayer("999"))
                .expectErrorMatches(err -> err instanceof PlayerNotFoundException &&
                        err.getMessage().equals("Player not found with id: 999"))
                .verify();

        verify(playerRepository).findById("999");
    }

    @Test
    void testUpdatePlayer() {
        when(playerRepository.findById("1")).thenReturn(Mono.just(testPlayer));
        when(playerRepository.save(testPlayer)).thenReturn(Mono.just(testPlayer));

        StepVerifier.create(playerService.updatePlayer("1", testPlayer))
                .expectNext(testPlayer)
                .verifyComplete();

        verify(playerRepository).findById("1");
        verify(playerRepository).save(testPlayer);
    }

    @Test
    void testUpdatePlayerNotFound() {
        when(playerRepository.findById("999")).thenReturn(Mono.empty());

        StepVerifier.create(playerService.updatePlayer("999", testPlayer))
                .expectErrorMatches(err -> err instanceof PlayerNotFoundException &&
                        err.getMessage().equals("Player not found with id: 999"))
                .verify();

        verify(playerRepository).findById("999");
        verify(playerRepository, never()).save(any());
    }

    @Test
    void testDeletePlayer() {
        when(playerRepository.findById("1")).thenReturn(Mono.just(testPlayer));
        when(playerRepository.deleteById("1")).thenReturn(Mono.empty());

        StepVerifier.create(playerService.deletePlayer("1"))
                .expectNext(true)
                .verifyComplete();

        verify(playerRepository).findById("1");
        verify(playerRepository).deleteById("1");
    }

    @Test
    void testDeletePlayerNotFound() {
        when(playerRepository.findById("999")).thenReturn(Mono.empty());

        StepVerifier.create(playerService.deletePlayer("999"))
                .expectErrorMatches(err -> err instanceof PlayerNotFoundException &&
                        err.getMessage().equals("Player not found with id: 999"))
                .verify();

        verify(playerRepository).findById("999");
        verify(playerRepository, never()).deleteById((String) any());
    }

}