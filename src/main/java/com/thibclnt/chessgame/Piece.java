package com.thibclnt.chessgame;

import java.util.Set;

/**
 * Base abstract class to be extended for creating pieces in board games. A piece has the general following attributes :
 * <ul>
 * <li> The {@link Board} on which the piece is (when alive) </li>
 * <li> The {@link Player} who 'owns' the piece </li>
 * <li> A position {@link Pos} on the board (can be null) </li>
 * <li> A state among the followings : DEAD, ALIVE, PROMOTED, ERROR (enum {@link PieceState}) </li>
 * </ul>
 */
public abstract class Piece {

    /**
     * Enumeration for the general possible states of a piece.
     * <ul>
     * <li> DEAD: When the piece is dead, ie eliminated </li>
     * <li> ALIVE: When the piece is on the board </li>
     * <li> PROMOTED: When a piece was replaced by another one </li>
     * <li> ERROR: When creation of a new piece or move fails </li>
     * </ul>
     */
    public enum PieceState {DEAD, ALIVE, PROMOTED, ERROR}

    /** A state among the followings : DEAD, ALIVE, PROMOTED, ERROR (enum {@link PieceState}) */
    protected PieceState state;
    /** Position {@link Pos} on the board (can be null) */
    protected Pos pos;
    /** The {@link Player} who 'owns' the piece */
    protected final Player player;
    /** The {@link Board} on which the piece is (when alive) */
    protected final Board board;

    /**
     * Only constructor for a {@link Piece}. By default, the piece state is set to ALIVE, unless an error is encountered.
     * In this case, a RuntimeException will be thrown and the state will be set to ERROR
     *
     * @param board The {@link Board} on which the piece is (when alive). Can't be null.
     * @param player The {@link Player} who 'owns' the piece. Can't be null.
     * @param pos The piece's position {@link Pos} on the board. Can be null.
     */
    public Piece(Board board, Player player, Pos pos) {
        this.board = board;


        if (this.board.isPosValid(pos))
        {
            this.pos = pos;
        }
        else
        {
            this.state = PieceState.ERROR;
            Exception e = new RuntimeException("A piece cannot be created at " + pos);
            e.printStackTrace();
        }

        this.board.addPiece(this);
        this.player = player;

        this.setState(this.player == null || this.pos == null ? PieceState.ERROR : PieceState.ALIVE);
    }

    /**
     * Must return the possible moves of piece as a {@link Set} of {@link Pos}.
     */
    public abstract Set<Pos> getLegalMoves();

    /**
     * Set the position of the piece, without any checking if this position is correct or not.
     *
     * @see Pos
     * @param position New position of the piece
     */
    public void setPos(Pos position) {
        this.pos = position;
    }

    /**
     * Move the piece to a new position. The main difference with {@link Piece#setPos(Pos)} is that if the position is not
     * valid, ie not on the board, onto another friend piece or null. In this cas, a RuntimeException is thrown. Another
     * notable difference is that it can be used to kill enemy's pieces.
     *
     * @see Pos
     * @see Board
     *
     * @param position New {@link Pos} of the piece
     */
    public void move(Pos position) {
        // Check if the move kills a piece (and kills it)
        Piece toKill = this.board.getPieceAt(position);
        if (toKill != null && toKill.player.getColor() != this.player.getColor()) {
            toKill.setState(PieceState.DEAD);
        }

        // Throw an exception if the movement is not valid
        if (this.board.isPosValid(position))
        {
            this.pos = position;
        }
        else
        {
            Exception e = new RuntimeException("Piece at " + this.pos + " cannot be moved to " + position);
            e.printStackTrace();
        }
    }

    /**
     * Set the {@link Piece#state} of the piece. If the state is not alive, the piece's position is set to null
     *
     * @see PieceState
     *
     * @param state New state of the pos among the following : DEAD, ALIVE, PROMOTED or ERROR.
     */
    public void setState(PieceState state) {
        this.state = state;
        if (this.state == PieceState.DEAD || this.state == PieceState.PROMOTED || this.state == PieceState.ERROR) {
            this.pos = null;
        }
    }

    /** {@link Piece#pos} */
    public Pos getPos() {
        return this.pos;
    }

    /** {@link Piece#player} */
    public Player getPlayer() { return this.player; }

    /** {@link Piece#board} */
    public Board getBoard() {
        return this.board;
    }

    /** {@link Piece#state} */
    public PieceState getState() { return this.state; }

    @Override
    public String toString() {
        String posStr = (pos == null) ? "NULL" : "(" + pos.getX() + ", " + pos.getY() + ")";

        return "{" +
                "state=" + state +
                ", pos=" + posStr +
                ", player=" + player.getName() +
                '}';
    }
}