package org.blackjack.advice;

import org.blackjack.exceptions.*;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GameFinishedException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleGameFinished(GameFinishedException ex, ServerWebExchange exchange) {
        return buildResponse(HttpStatus.BAD_REQUEST, "GAME_FINISHED", ex.getMessage());
    }

    @ExceptionHandler(CardNullException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleCardNull(CardNullException ex, ServerWebExchange exchange) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "CARD_NULL", ex.getMessage());
    }

    @ExceptionHandler(DeckEmptyException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleDeckEmpty(DeckEmptyException ex, ServerWebExchange exchange) {
        return buildResponse(HttpStatus.BAD_REQUEST, "DECK_EMPTY", ex.getMessage());
    }

    @ExceptionHandler(GameNotFoundException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleGameNotFound(GameNotFoundException ex, ServerWebExchange exchange) {
        return buildResponse(HttpStatus.NOT_FOUND, "GAME_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(PlayerNotFoundException.class)
    public Mono<ResponseEntity<ErrorResponse>> handlePlayerNotFound(PlayerNotFoundException ex, ServerWebExchange exchange) {
        return buildResponse(HttpStatus.NOT_FOUND, "PLAYER_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> handleGeneric(Exception ex, ServerWebExchange exchange) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", ex.getMessage());
    }

    private Mono<ResponseEntity<ErrorResponse>> buildResponse(HttpStatus status, String code, String message) {
        return Mono.just(ResponseEntity.status(status).body(new ErrorResponse(code, message)));
    }

    public record ErrorResponse(String code, String message) {}

}