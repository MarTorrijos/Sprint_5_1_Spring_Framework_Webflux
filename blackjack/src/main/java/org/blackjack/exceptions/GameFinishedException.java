package org.blackjack.exceptions;

public class GameFinishedException extends RuntimeException {

  public GameFinishedException(String message) {
      super(message);
  }

}