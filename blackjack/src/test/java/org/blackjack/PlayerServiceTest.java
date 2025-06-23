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
        testPlayer = new Player(1, "Player", 100);
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
        when(playerRepository.findById(1)).thenReturn(Mono.just(testPlayer));

        Mono<Player> resultMono = playerService.getPlayer(1);

        StepVerifier.create(resultMono)
                .expectNext(testPlayer)
                .verifyComplete();

        verify(playerRepository, times(1)).findById(1);
    }


    @Test
    void testUpdatePlayer() {
        when(playerRepository.save(testPlayer)).thenReturn(Mono.just(testPlayer));

        Mono<Player> resultMono = playerService.updatePlayer(testPlayer);

        StepVerifier.create(resultMono)
                .expectNext(testPlayer)
                .verifyComplete();

        verify(playerRepository, times(1)).save(testPlayer);
    }

    @Test
    void testDeletePlayer() {
        when(playerRepository.deleteById(testPlayer.getId())).thenReturn(Mono.empty());

        Mono<Void> resultMono = playerService.deletePlayer(1);

        StepVerifier.create(resultMono)
                .verifyComplete();

        verify(playerRepository, times(1)).deleteById(testPlayer.getId());
    }

}