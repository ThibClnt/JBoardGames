package com.thibclnt.chessgame;

/**
 * Base interface for all the board games in the app. It contains general methods to be implemented in these games, in
 * order to start, stop, reset the game or to do basic operations such as checking if a player has won or return to whom
 * the turn is.
 */
public interface Game {

    /**
     * Method to call when starting the game. It may start a game loop.
     */
    void start();

    /**
     * Method to call when stopping a game. It may inform the player that the game is being stopped and stop the game loop.
     */
    void stop();

    /**
     * Method to reset the game, for example in order to play again. It may stop the loop, reinitialise some attributes
     * and start a new loop.
     */
    void reset();

    /**
     * Method to know to whom turn it is.
     * @return The {@link Player} to whom the turn is.
     */
    Player getPlayerTurn();

    /**
     * Method to check if the game must end (with a win or a draw). It may display the result of the game if it ends then
     * call stop or reset.
     */
    void checkWinCondition();
}
