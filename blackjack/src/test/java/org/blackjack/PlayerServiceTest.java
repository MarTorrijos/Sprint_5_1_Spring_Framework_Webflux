package org.blackjack;

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

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerService playerService;
    private Player testPlayer;

    @BeforeEach
    void setUp() {
        testPlayer = new Player("1", "Player", 100);
    }

    @Test
    void testAddPlayer() {
        when(playerRepository.save(testPlayer)).thenReturn(Mono.just(testPlayer));

        Mono<Player> resultMono = playerService.addPlayer(testPlayer);

        StepVerifier.create(resultMono)
                .expectNext(testPlayer)
                .verifyComplete();

        verify(playerRepository, times(1)).save(testPlayer);
    }

    @Test
    void testGetPlayer() {
        when(playerRepository.findById(testPlayer.getId())).thenReturn(Mono.just(testPlayer));

        Mono<Player> resultMono = playerService.getPlayer(testPlayer.getId());

        StepVerifier.create(resultMono)
                .expectNext(testPlayer)
                .verifyComplete();

        verify(playerRepository, times(1)).findById(testPlayer.getId());
    }

    @Test
    void testUpdatePlayer() {
        when(playerRepository.findById(testPlayer.getId())).thenReturn(Mono.just(testPlayer));
        when(playerRepository.save(testPlayer)).thenReturn(Mono.just(testPlayer));

        Mono<Player> resultMono = playerService.updatePlayer(testPlayer.getId(), testPlayer);

        StepVerifier.create(resultMono)
                .expectNext(testPlayer)
                .verifyComplete();

        verify(playerRepository, times(1)).findById(testPlayer.getId());
        verify(playerRepository, times(1)).save(testPlayer);
    }

    @Test
    void testDeletePlayer() {
        when(playerRepository.findById(testPlayer.getId())).thenReturn(Mono.just(testPlayer));
        when(playerRepository.deleteById(testPlayer.getId())).thenReturn(Mono.empty());

        Mono<Boolean> resultMono = playerService.deletePlayer(testPlayer.getId());

        StepVerifier.create(resultMono)
                .expectNext(true)
                .verifyComplete();

        verify(playerRepository, times(1)).findById(testPlayer.getId());
        verify(playerRepository, times(1)).deleteById(testPlayer.getId());
    }

}