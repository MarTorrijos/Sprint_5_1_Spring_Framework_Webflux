package org.blackjack;

import org.blackjack.model.Player;
import org.blackjack.repositories.PlayerRepository;
import org.blackjack.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerService playerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // TODO
    // testAddPlayer()

    @Test
    void testGetPlayer() {
        Player player = new Player(1, "Player1", 100);
        when(playerRepository.findById(1)).thenReturn(Mono.just(player));

        Mono<Player> resultMono = playerService.getPlayer(1);

        StepVerifier.create(resultMono)
                .expectNextMatches(player1 -> player1.getName().equals("Player1"))
                .verifyComplete();

        verify(playerRepository, times(1)).findById(1);
    }

    // TODO
    // testUpdatePlayer()

    // TODO
    // testDeletePlayer()

}