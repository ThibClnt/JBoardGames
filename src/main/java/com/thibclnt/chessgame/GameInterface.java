package com.thibclnt.chessgame;

import java.util.Set;

/**
 * Interface that must be implemented to allow the user to play an attached {@link Game}.
 */
public interface GameInterface{
    /**
     * Initialisation function of the {@link ChessGui} class, called from {@link ChessGame} class whenever the chess game is created.
     * It disposes all widgets in the GUI app frame, set widget's properties and action listeners if they have one.
     */
    void init();

    /**
     * Update the display of the game.
     */
    void draw();

    /**
     * Ask for a move on the board.
     */
    void askForMove();

    /**
     * Is called when the game is won by a {@link Player}, in order to display it.
     * @param player {@link Player} who won
     */
    void declareWinner(Player player);

    /**
     * Is called when the game is ended with a draw, in order to display it.
     */
    void declareNull();

    /**
     * Display the moves that a given piece can do. These moves have to be computed by the {@link Game} implementation.
     * @param legalMoves {@link Set} of the {@link Pos} to which a piece can move.
     */
    void displayLegalMove(Set<Pos> legalMoves);
}
