package com.thibclnt.chessgame;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * King piece for chess. This is the most important piece of the game, which can't die. If the king can't be freed from
 * attack, there is Checkmate and the owner of the king loses the game.
 * A King can only move by 1 square in each direction (except when castling).
 * Castling is supported in this class.
 */
public class King extends ChessPiece{

    /** True if the pawn has done its first move - false otherwise. Useful for castling */
    private boolean hasAlreadyMoved;

    /**
     * By default, the piece state is set to ALIVE, unless an error is encountered.
     * In this case, a RuntimeException will be thrown and the state will be set to ERROR
     *
     * @param board The {@link Board} on which the piece is (when alive). Can't be null.
     * @param player The {@link Player} who 'owns' the piece. Can't be null.
     * @param pos The piece's position {@link Pos} on the board. Can be null.
     */
    public King(ChessBoard board, Player player, Pos pos) {
        super(board, player, pos, ChessPieceType.KING);
    }

    /**
     * By default, the piece state is set to ALIVE, unless an error is encountered.
     * In this case, a RuntimeException will be thrown and the state will be set to ERROR
     *
     * @param board The {@link Board} on which the piece is (when alive). Can't be null.
     * @param player The {@link Player} who 'owns' the piece. Can't be null.
     * @param x Horizontal position on the ChessBoard between 1 and 8 (inclusive) from left to right
     * @param y Vertical position on the ChessBoard between 1 and 8 (inclusive) from bottom to top
     */
    public King(ChessBoard board, Player player, int x, int y) {
        super(board, player, new Pos(x, y), ChessPieceType.KING);
    }

    @Override
    public void move(Pos position) {
        // Check for castle moves
        if (position.getX() - pos.getX() > 1) {
            castleShort();
        } else if (position.getX() - pos.getX() < -1) {
            castleLong();
        }

        super.move(position);
        this.hasAlreadyMoved = true;
    }

    /**
     * Castle short when the king is moved two squares to the right. Please check short castling can be done before with
     * {@link King#canCastleShort()}. If the castling is not possible, a Runtime Exception will be thrown.
      */
    private void castleShort() {
        ChessPiece rook = board.getPieceAt(new Pos(8, pos.getY()));

        if (rook == null || !(rook instanceof Rook) || rook.player != this.player) {
            throw new RuntimeException("Can't castle short.\n");
        }

        rook.move(new Pos(pos.getX() + 1, pos.getY()));
    }

    /**
     * Castle long when the king is moved two squares to the left. Please check long castling can be done before with
     * {@link King#canCastleLong()}. If the castling is not possible, a Runtime Exception will be thrown.
     */
    private void castleLong() {
        ChessPiece rook = board.getPieceAt(new Pos(1, pos.getY()));

        if (rook == null || !(rook instanceof Rook) || rook.player != this.player) {
            throw new RuntimeException("Can't castle short.\n");
        }

        rook.move(new Pos(pos.getX() - 1, pos.getY()));
    }

    /**
     * Check if a long castle can be done (long means to the left). Castling can be done only when the king and the according
     * rook never had moved, and the king and the two squares on its left are not threatened by enemy's pieces.
     *
     * @return true if long castling can be done, false elsewhere.
     */
    public boolean canCastleShort() {
        // Different strategy for black and white
        int y = (this.player.getColor() == Player.COLOR.WHITE) ? 1 : 8;
        ChessPiece piece = board.getPieceAt(new Pos(8, y));
        Rook rook;

        // False if squares are not free
        Pos[] posToCheck = {new Pos(6, y), new Pos(7, y)};
        for (Pos pos : posToCheck) {
            if (this.board.getPieceAt(pos) != null)
                return false;
        }

        // False if the rook is not at the right place
        if (piece == null || piece.getType() != ChessPieceType.ROOK)
            return false;
        else
            rook = (Rook)piece;

        // False if the king or the rook has already moved
        if (rook.hasAlreadyMoved() || this.hasAlreadyMoved)
            return false;

        // False if one of the squares the king goes through is in check
        // Otherwise, all the conditions are passed
        return !board.isInCheck(this.pos, this.player.getEnemy()) &&
                !board.isInCheck(new Pos(6, this.pos.getY()), this.player.getEnemy()) &&
                !board.isInCheck(new Pos(7, this.pos.getY()), this.player.getEnemy());
    }

    /**
     * Check if a long castle can be done (long means to the left). Castling can be done only when the king and the according
     * rook never had moved, and the king and the two squares on its right are not threatened by enemy's pieces.
     *
     * @return true if long castling can be done, false elsewhere.
     */
    public boolean canCastleLong(){
        // Different strategy for black and white
        int y = (this.player.getColor() == Player.COLOR.WHITE) ? 1 : 8;
        ChessPiece piece = board.getPieceAt(new Pos(1, y));
        Rook rook;

        // False if squares are not free
        Pos[] posToCheck = {new Pos(2, y), new Pos(3, y), new Pos(4, y)};
        for (Pos pos : posToCheck) {
            if (this.board.getPieceAt(pos) != null)
                return false;
        }

        // False if the rook is not at the right place
        if (piece == null || piece.getType() != ChessPieceType.ROOK)
            return false;
        else
            rook = (Rook)piece;

        // False if the king or the rook has already moved
        if (rook.hasAlreadyMoved() || this.hasAlreadyMoved)
            return false;

        // False if one of the squares the king goes through is in check
        // Otherwise, all the conditions are passed
        return !board.isInCheck(this.pos, this.player.getEnemy()) &&
                !board.isInCheck(new Pos(4, this.pos.getY()), this.player.getEnemy()) &&
                !board.isInCheck(new Pos(3, this.pos.getY()), this.player.getEnemy());
    }

    /** @see King#hasAlreadyMoved */
    public boolean hasAlreadyMoved(){
        return this.hasAlreadyMoved;
    }

    @Override
    public Set<Pos> getAttacked() {
        Set<Pos> attacked = new HashSet<>(Arrays.asList(
                new Pos(this.pos.getX() + 1, this.pos.getY()    ),
                new Pos(this.pos.getX() + 1, this.pos.getY() + 1),
                new Pos(this.pos.getX()    , this.pos.getY() + 1),
                new Pos(this.pos.getX() - 1, this.pos.getY() + 1),
                new Pos(this.pos.getX() - 1, this.pos.getY()    ),
                new Pos(this.pos.getX() - 1, this.pos.getY() - 1),
                new Pos(this.pos.getX()    , this.pos.getY() - 1),
                new Pos(this.pos.getX() + 1, this.pos.getY() - 1)
        ));

        attacked.removeIf(pos -> !board.isOnBoard(pos));
        return attacked;
    }

    @Override
    protected Set<Pos> getLegalMoves(boolean ignoreKing) {
        // Retrieve the coordinates of the piece
        int x = getPos().getX();
        int y = getPos().getY();

        // Add the possibility to move one square around, minus moves outside the board
        Set<Pos> legal_moves = getAttacked();

        // Check if castles can be done. If it can, add the castle move in legal moves.
        if (canCastleShort()) {
            legal_moves.add(new Pos(x + 2, y));
        }
        if (canCastleLong()) {
            legal_moves.add(new Pos(x - 2, y));
        }

        // Remove move on pieces that can't be killed, ie of the same player or a king
        legal_moves.removeIf(
                pos -> {
                    ChessPiece pieceAt = this.board.getPieceAt(pos);
                    if (pieceAt != null) {
                        return this.board.getPieceAt(pos).getType() == ChessPieceType.KING
                                || this.board.getPieceAt(pos).player == this.player;
                    } else {
                        return false;
                    }
                }
        );

        // Retrieve possibilities where the king would be in check
        legal_moves.removeIf(pos1 ->getBoard().isStillInCheck(player, this, pos1));

        return legal_moves;
    }
}
