package com.thibclnt.chessgame;

import java.util.HashSet;
import java.util.Set;

import static java.lang.Math.abs;

/**
 * Pawn piece for chess. After its first move the Pawn may only advance one square at a time. The Pawn captures by
 * moving diagonally one square forward in each direction. The Pawn cannot move or capture backwards.
 * This class also supports the specific rules of the Pawn :
 *<p>
 *     PROMOTION : If a White Pawn reaches the 8th (or 1st with Black) rank of the board, it must be exchanged. It can be promoted
 *     to a Queen, Rook, Bishop or Knight of its own colour. But never to a King or another pawn.
 *</p> <p>
 *     EN PASSANT : The possibility of en passant Pawn capture arises when the opponent’s Pawn has just moved from its starting
 *     position two squares ahead and our Pawn is next to it. This kind of capture is only possible at this time and
 *     cannot be done later.
 *</p>
 */
public class Pawn extends ChessPiece{

    /** True if the pawn has done its first move - false otherwise. Useful for en passant and two squares moves */
    private boolean hasAlreadyMoved = false;

    /**
     * By default, the piece state is set to ALIVE, unless an error is encountered.
     * In this case, a RuntimeException will be thrown and the state will be set to ERROR
     *
     * @param board The {@link Board} on which the piece is (when alive). Can't be null.
     * @param player The {@link Player} who 'owns' the piece. Can't be null.
     * @param pos The piece's position {@link Pos} on the board. Can be null.
     */
    public Pawn(ChessBoard board, Player player, Pos pos) {
        super(board, player, pos, ChessPieceType.PAWN);
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
    public Pawn(ChessBoard board, Player player, int x, int y) {
        super(board, player, new Pos(x, y), ChessPieceType.PAWN);
    }

    @Override
    public void move(Pos position) {
        Pos old = this.pos;

        // Check if kill enPassant (and kill if necessary)
        ChessPiece moveOn = this.board.getPieceAt(position);
        if (old.getX() - position.getX() != 0 && moveOn == null) {
            killEnPassant(old, position);
        }

        super.move(position);
        this.hasAlreadyMoved = true;
        this.checkForPromotion();
    }

    /** @see Pawn#hasAlreadyMoved */
    public boolean hasAlreadyMoved(){
        return this.hasAlreadyMoved;
    }

    /**
     * Kill en passant while moving from oldPos to newPos. Please check before if this move can be done with {@link Pawn#checkForEnPassantMove()}.
     * If the move can not be done, a Runtime Exception will be thrown.
     *
     * @param oldPos {@link Pos} from where the pawn move
     * @param newPos {@link Pos} to where the pawn move
     */
    public void killEnPassant(Pos oldPos, Pos newPos){
        ChessPiece toKill = this.board.getPieceAt(new Pos(newPos.getX(), oldPos.getY()));

        if (toKill == null || toKill.getType() != ChessPieceType.PAWN || toKill.player != player.getEnemy()) {
            throw new RuntimeException("Can't kill en passant piece at pos {" + newPos.getX() + ", " + oldPos.getY() + "}.\n");
        }

        toKill.setState(PieceState.DEAD);
    }

    @Override
    public Set<Pos> getAttacked() {
        HashSet<Pos> diagonals =  new HashSet<>();
        int direction = (this.player.getColor() == Player.COLOR.WHITE) ? 1 : -1;

        // Retrieving pos in diagonal
        Pos posL = new Pos(this.pos.getX() - 1, this.pos.getY() + direction);
        Pos posR = new Pos(this.pos.getX() + 1, this.pos.getY() + direction);

        // Check if diagonals are on the board
        if (board.isOnBoard(posL)) {
            diagonals.add(posL);
        }
        if (board.isOnBoard(posR)) {
            diagonals.add(posR);
        }

        return diagonals;
    }

    @Override
    protected Set<Pos> getLegalMoves(boolean ignoreKing) {
        HashSet<Pos> legal_moves = new HashSet<>();

        // Retrieve the coordinates of the piece
        int x = this.pos.getX();
        int y = this.pos.getY();

        // If the pawn is white, it moves toward the top of the board, otherwise, it moves towards the bottom
        int direction = (this.player.getColor() == Player.COLOR.WHITE) ? 1 : -1;

        // Can move only if there is no blocking piece
        if (this.board.getPieceAt(new Pos(x, y + direction)) == null) {
            legal_moves.add(new Pos(x, y + direction));

            // Add the possibility of moving of 2 squares if the pawn has not already moved
            if (!this.hasAlreadyMoved && this.board.getPieceAt(new Pos(x, y + 2 * direction)) == null) {
                legal_moves.add(new Pos(x, y + 2 * direction));
            }
        }

        // Add the possibility of moving en passant moves
        legal_moves.addAll(checkForEnPassantMove());

        // Add the possibility of moving in diagonals if there are pieces to kill
        legal_moves.addAll(checkForDiagonals());

        // Remove some possibilities if the king is in check
        if (!ignoreKing)
            legal_moves.removeIf(pos1 -> board.isStillInCheck(player, this, pos1));

        return legal_moves;
    }

    /**
     * Check if the pawn can do a 'en passant move'.
     * <p>
     * EN PASSANT : The possibility of en passant Pawn capture arises when the opponent’s Pawn has just moved from its starting
     *  position two squares ahead and our Pawn is next to it. This kind of capture is only possible at this time and
     *  cannot be done later. </p>
     *
     * @return true if a 'en passant move' can be done, false elsewhere.
     */
    private Set<Pos> checkForEnPassantMove() {
        HashSet<Pos> enPassantLegalMoves = new HashSet<>();

        // If the pawn is not at the right position, return empty set
        if (this.player.getColor() == Player.COLOR.WHITE && this.pos.getY() != 5 ||
                this.player.getColor() == Player.COLOR.BLACK && this.pos.getY() != 4)
        {
            return enPassantLegalMoves;
        }

        // Pieces that could be killed, if they exist
        ChessPiece pieceL = this.board.getPieceAt(new Pos(this.pos.getX() - 1, this.pos.getY()));
        ChessPiece pieceR = this.board.getPieceAt(new Pos(this.pos.getX() + 1, this.pos.getY()));
        int direction = (this.player.getColor() == Player.COLOR.WHITE) ? 1 : -1;

        ChessPiece lastMovePiece = board.getGame().getLastMove().piece;
        boolean isLastMoveTwoSquares =
                abs(board.getGame().getLastMove().to.getY() - board.getGame().getLastMove().from.getY()) == 2;

        // Check if there is a pawn of opposite color at x-1, that just move two squares
        if (pieceL == lastMovePiece && lastMovePiece.getType() == ChessPieceType.PAWN
                && isLastMoveTwoSquares)
        {
            enPassantLegalMoves.add(new Pos(this.pos.getX() - 1, this.pos.getY() + direction));
        }

        // Check if there is a pawn of opposite color at x+1, that just move two squares
        if (pieceR == lastMovePiece && lastMovePiece.getType() == ChessPieceType.PAWN
                && isLastMoveTwoSquares)
        {
            enPassantLegalMoves.add(new Pos(this.pos.getX() + 1, this.pos.getY() + direction));
        }
        return enPassantLegalMoves;
    }

    /**
     * Check if the pawn can kill another piece in diagonal.
     *
     * @return A {@link Set} of {@link Pos} the pawn can move on in diagonal.
     */
    private Set<Pos> checkForDiagonals() {
        HashSet<Pos> diagonals =  new HashSet<>();
        int direction = (this.player.getColor() == Player.COLOR.WHITE) ? 1 : -1;

        // Retrieving pieces in diagonal, if they exist
        ChessPiece pieceL = this.board.getPieceAt(new Pos(this.pos.getX() - 1, this.pos.getY() + direction));
        ChessPiece pieceR = this.board.getPieceAt(new Pos(this.pos.getX() + 1, this.pos.getY() + direction));

        // Check if there are pieces and if they can be killed (different color + not a king)
        if (pieceL != null && pieceL.player.getColor() != this.player.getColor() && pieceL.getType() != ChessPieceType.KING) {
            diagonals.add(new Pos(this.pos.getX() - 1, this.pos.getY() + direction));
        }
        if (pieceR != null && pieceR.player.getColor() != this.player.getColor() && pieceR.getType() != ChessPieceType.KING) {
            diagonals.add(new Pos(this.pos.getX() + 1, this.pos.getY() + direction));
        }

        return diagonals;
    }

    /**
     * Check if the pawn is at the end of the board - so if a promotion must be done. In this last case, the promotion is
     * then asked.
     */
    private void checkForPromotion(){

        // Check if the pawn is at one end of the board. If the pawn is white, the end is at the top (y = 8), otherwise
        // it is at the bottom (y = 1)
        if ((this.player.getColor() == Player.COLOR.WHITE && this.pos.getY() == 8)
                || (this.player.getColor() == Player.COLOR.BLACK && this.pos.getY() == 1)) {
            ChessPieceType promoteTo = this.board.getGame().getGameInterface().askForPromotion(this);
            this.board.promote(this, promoteTo);
            this.setState(PieceState.PROMOTED);
        }
    }
}
