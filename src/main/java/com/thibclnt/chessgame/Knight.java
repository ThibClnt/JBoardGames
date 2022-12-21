package com.thibclnt.chessgame;

import java.util.HashSet;
import java.util.Set;

/**
 * Knight piece for chess. The Knight can jump to any square in L shape (2 squares then 1 aside). This is the only piece
 * that can jump over a piece in its way.
 */
public class Knight extends ChessPiece{

    /**
     * By default, the piece state is set to ALIVE, unless an error is encountered.
     * In this case, a RuntimeException will be thrown and the state will be set to ERROR
     *
     * @param board The {@link Board} on which the piece is (when alive). Can't be null.
     * @param player The {@link Player} who 'owns' the piece. Can't be null.
     * @param pos The piece's position {@link Pos} on the board. Can be null.
     */
    public Knight(ChessBoard board, Player player, Pos pos) {
        super(board, player, pos, ChessPieceType.KNIGHT);
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
    public Knight(ChessBoard board, Player player, int x, int y) {
        super(board, player, new Pos(x, y), ChessPieceType.KNIGHT);
    }

    @Override
    protected Set<Pos> getLegalMoves(boolean ignoreKing) {
        HashSet<Pos> legal_moves = new HashSet<>();

        // Retrieve the coordinates of the piece
        int x = getPos().getX();
        int y = getPos().getY();

        // Add moves in 'L'
        legal_moves.add(new Pos(x + 2 , y + 1));
        legal_moves.add(new Pos(x + 2 , y - 1));

        legal_moves.add(new Pos(x - 2 , y + 1));
        legal_moves.add(new Pos(x - 2 , y - 1));

        legal_moves.add(new Pos(x + 1 , y + 2));
        legal_moves.add(new Pos(x + 1 , y - 2));

        legal_moves.add(new Pos(x - 1 , y + 2));
        legal_moves.add(new Pos(x - 1 , y - 2));

        // Remove possibilities if not on board or on a king or on a piece of the same player
        legal_moves.removeIf(pos -> !this.board.isOnBoard(pos));
        legal_moves.removeIf(
                pos -> {
                    ChessPiece pieceAt = this.board.getPieceAt(pos);
                    if (pieceAt != null) {
                        return (this.board.getPieceAt(pos).getType() == ChessPieceType.KING && !ignoreKing)
                                || this.board.getPieceAt(pos).player == this.player;
                    } else {
                        return false;
                    }
                }
        );

        // Remove some possibilities if the king is in check
        King king = (King) ChessBoard.getPiecesByType(this.board.getPiecesByPlayer(player), ChessPiece.ChessPieceType.KING).toArray()[0];
        if (!ignoreKing)
            legal_moves.removeIf(pos1 -> board.isStillInCheck(player, this, pos1));

        return legal_moves;
    }
}
