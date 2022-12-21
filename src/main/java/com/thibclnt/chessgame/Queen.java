package com.thibclnt.chessgame;

import java.util.HashSet;
import java.util.Set;

/** Queen piece for chess. It is the most valuable piece, as it combines moves in diagonal and moves in rows and columns. */
public class Queen extends ChessPiece{

    /**
     * By default, the piece state is set to ALIVE, unless an error is encountered.
     * In this case, a RuntimeException will be thrown and the state will be set to ERROR
     *
     * @param board The {@link Board} on which the piece is (when alive). Can't be null.
     * @param player The {@link Player} who 'owns' the piece. Can't be null.
     * @param pos The piece's position {@link Pos} on the board. Can be null.
     */
    public Queen(ChessBoard board, Player player, Pos pos) {
        super(board, player, pos, ChessPieceType.QUEEN);
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
    public Queen(ChessBoard board, Player player, int x, int y) {
        super(board, player, new Pos(x, y), ChessPieceType.QUEEN);
    }

    @Override
    protected Set<Pos> getLegalMoves(boolean ignoreKing) {
        HashSet<Pos> legal_moves = new HashSet<>();
        ChessPiece pieceAt;

        // Retrieve the coordinates of the piece
        int x = getPos().getX();
        int y = getPos().getY();
        Pos temp_pos;

        // Add move towards the top right
        temp_pos = new Pos(x + 1, y + 1);
        pieceAt = board.getPieceAt(temp_pos);
        while (board.isOnBoard(temp_pos)
                && (pieceAt == null || (pieceAt.player == this.player.getEnemy()
                && (pieceAt.getType() != ChessPieceType.KING || ignoreKing)))){
            legal_moves.add(new Pos(temp_pos));
            if ((pieceAt != null &&  pieceAt.player == this.player.getEnemy()))
                break;
            temp_pos.setX(temp_pos.getX() + 1);
            temp_pos.setY(temp_pos.getY() + 1);
            pieceAt = board.getPieceAt(temp_pos);
        }

        // Add move towards the bottom left
        temp_pos = new Pos(x - 1, y - 1);
        pieceAt = board.getPieceAt(temp_pos);
        while (board.isOnBoard(temp_pos)
                && (pieceAt == null || (pieceAt.player == this.player.getEnemy()
                && (pieceAt.getType() != ChessPieceType.KING || ignoreKing)))){
            legal_moves.add(new Pos(temp_pos));
            if ((pieceAt != null &&  pieceAt.player == this.player.getEnemy()))
                break;
            temp_pos.setX(temp_pos.getX() - 1);
            temp_pos.setY(temp_pos.getY() - 1);
            pieceAt = board.getPieceAt(temp_pos);
        }

        // Add move towards the top left
        temp_pos = new Pos(x - 1, y + 1);
        pieceAt = board.getPieceAt(temp_pos);
        while (board.isOnBoard(temp_pos)
                && (pieceAt == null || (pieceAt.player == this.player.getEnemy()
                && (pieceAt.getType() != ChessPieceType.KING || ignoreKing)))){
            legal_moves.add(new Pos(temp_pos));
            if ((pieceAt != null &&  pieceAt.player == this.player.getEnemy()))
                break;
            temp_pos.setX(temp_pos.getX() - 1);
            temp_pos.setY(temp_pos.getY() + 1);
            pieceAt = board.getPieceAt(temp_pos);
        }

        // Add move towards the bottom right
        temp_pos = new Pos(x + 1, y - 1);
        pieceAt = board.getPieceAt(temp_pos);
        while (board.isOnBoard(temp_pos)
                && (pieceAt == null || (pieceAt.player == this.player.getEnemy()
                && (pieceAt.getType() != ChessPieceType.KING || ignoreKing)))){
            legal_moves.add(new Pos(temp_pos));
            if ((pieceAt != null &&  pieceAt.player == this.player.getEnemy()))
                break;
            temp_pos.setX(temp_pos.getX() + 1);
            temp_pos.setY(temp_pos.getY() - 1);
            pieceAt = board.getPieceAt(temp_pos);
        }

        // Add moves towards the right
        temp_pos = new Pos(x + 1, y);
        pieceAt = board.getPieceAt(temp_pos);
        while (board.isOnBoard(temp_pos)
                && (pieceAt == null || (pieceAt.player == this.player.getEnemy()
                && (pieceAt.getType() != ChessPieceType.KING || ignoreKing)))){
            legal_moves.add(new Pos(temp_pos));
            if ((pieceAt != null &&  pieceAt.player == this.player.getEnemy()))
                break;
            temp_pos.setX(temp_pos.getX() + 1);
            pieceAt = board.getPieceAt(temp_pos);
        }

        // Add moves towards the left
        temp_pos = new Pos(x - 1, y);
        pieceAt = board.getPieceAt(temp_pos);
        while (board.isOnBoard(temp_pos)
                && (pieceAt == null || (pieceAt.player == this.player.getEnemy()
                && (pieceAt.getType() != ChessPieceType.KING || ignoreKing)))){
            legal_moves.add(new Pos(temp_pos));
            if ((pieceAt != null &&  pieceAt.player == this.player.getEnemy()))
                break;
            temp_pos.setX(temp_pos.getX() - 1);
            pieceAt = board.getPieceAt(temp_pos);
        }

        // Add move towards the top
        temp_pos = new Pos(x, y + 1);
        pieceAt = board.getPieceAt(temp_pos);
        while (board.isOnBoard(temp_pos)
                && (pieceAt == null || (pieceAt.player == this.player.getEnemy()
                && (pieceAt.getType() != ChessPieceType.KING || ignoreKing)))){
            legal_moves.add(new Pos(temp_pos));
            if ((pieceAt != null &&  pieceAt.player == this.player.getEnemy()))
                break;
            temp_pos.setY(temp_pos.getY() + 1);
            pieceAt = board.getPieceAt(temp_pos);
        }

        // Add move towards the bottom
        temp_pos = new Pos(x, y - 1);
        pieceAt = board.getPieceAt(temp_pos);
        while (board.isOnBoard(temp_pos)
                && (pieceAt == null || (pieceAt.player == this.player.getEnemy()
                && (pieceAt.getType() != ChessPieceType.KING || ignoreKing)))){
            legal_moves.add(new Pos(temp_pos));
            if ((pieceAt != null &&  pieceAt.player == this.player.getEnemy()))
                break;
            temp_pos.setY(temp_pos.getY() - 1);
            pieceAt = board.getPieceAt(temp_pos);
        }

        // Remove some possibilities if the king is in check
        if (!ignoreKing)
            legal_moves.removeIf(pos1 -> board.isStillInCheck(player, this, pos1));

        return legal_moves;
    }
}
