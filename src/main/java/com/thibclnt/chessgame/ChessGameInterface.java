package com.thibclnt.chessgame;

/**
 * Interface that must be implemented to allow the user to play an attached {@link ChessGame}. Add some chess specific
 * functions to {@link GameInterface}.
 */
public interface ChessGameInterface extends GameInterface {

    /**
     * Ask for a promotion when a pawn is at the end of the board.
     *
     * @param pawn Will be the pawn which is about to be promoted
     * @return {@link com.thibclnt.chessgame.ChessPiece.ChessPieceType} chosen to upgrade the pawn (Rook, Bishop, Knight, Queen).
     */
    ChessPiece.ChessPieceType askForPromotion(Pawn pawn);

    /**
     * Display when a player is in check (after the move of the player whose turn is, so its enemy is in check).
     */
    void tellCheck();
}
