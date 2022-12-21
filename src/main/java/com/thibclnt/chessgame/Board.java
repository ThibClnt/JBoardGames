package com.thibclnt.chessgame;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The base class for representing rectangular game boards. Should be extended for more functionalities. A board
 * contains pieces of type P which must be a child class of {@link Piece}. {@link Board}
 * must be constructed by specifying a size (width and height). Only one piece can be placed on each square,
 * which are represented positions {@link Pos} on the board.
 * <p>
 *     {@link Board} contains general methods to add, and get pieces (by state, player, position...). Pieces are added
 *     to the board during their instanciation. It is highly discouraged to add a Piece manually using
 *     <i>addPiece(Piece)</i> to the board.
 * </p>
 * @see Game
 * @see Piece
 * @see Pos
 */
public class Board<P extends Piece>{

    /** Width of the board = max <i>x</i> valid value. */
    private final int width;
    /** Height of the board = max <i>y</i> valid value. */
    private final int height;
    /** Collection ({@link HashSet}) of {@link Piece}s which are on the board (even "dead" ones). */
    protected final HashSet<P> pieces;

    /**
     * Only constructor of {@link Board}. The board is to consider as an array of squares, each squares having <i>x</i>
     * and <i>y</i> coordinates. In this board, positions can be represented through {@link Pos} instances, for which
     * <i>x</i> may vary between 1 and width, and <i>y</i> between 1 and height.
     *
     * @param width Width of the board : max value of x positions.
     * @param height Height of the board : max value of y positions.
     */
    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.pieces = new HashSet<>();
    }

    /**
     * Add a Piece to the board.
     * NOTE : If you want to add a piece to the board, please consider doing it via the constructor of the piece.
     * If the use of addPiece is really needed, please ensure that its position is valid.
     *
     * @param piece Piece added to the board.
     */
    void addPiece(P piece) {
        this.pieces.add(piece);
    }

    /**
     * Check if a position {@link Pos} is valid or not on the board. A valid position is a position where there is no
     * Piece, and which is in the bounds of the board. This method can be used to either ensure a position, for
     * exemple when adding a piece, of to filter a piece's legal moves.
     * true is returned when the position is valid, otherwise false is returned.
     * false.
     *
     * @param pos Position checked on the board.
     * @return true if pos is valid, otherwise false.
     */
    public boolean isPosValid(Pos pos) {
        // If there is no piece at pos, getPieceAt(pos) returns null
        boolean isPosFree = getPieceAt(pos) == null;

        return isPosFree
                && pos.getX() > 0 && pos.getX() <= this.width
                && pos.getY() > 0 && pos.getY() <= this.height;
    }

    /**
     * Return the Piece at the position {@link Pos} on the board, if there is one. This method is useful in
     * order to retrieve a piece, knowing its position, or to check if a square (pos) is free.
     * If no piece is found, return null.
     *
     * @param pos Position of the piece to retrieve.
     * @return The piece at the position pos, or null if none is found.
     */
    public P getPieceAt(Pos pos) {
        for (P piece : pieces) {
            if (piece.getPos() == null)
                continue;

            if (piece.getPos().equals(pos))
                return piece;
        }
        return null;
    }

    /**
     * Return the {@link Board#width} of the board
     * @return {@link Board#width} of the board
     */
    public int getWidth() {
        return width;
    }

    /**
     * Return the {@link Board#height} of the board
     * @return {@link Board#height} of the board
     */
    public int getHeight() {
        return height;
    }

    /**
     * Return a {@link Set} of all the {@link Piece}s on the board, even "dead" ones.
     * @return {@link Set} of all the {@link Piece}s on the board, even "dead" ones.
     */
    public Set<P> getAllPieces() {
        return pieces;
    }

    /**
     * Return a {@link Set} of all the pieces on the board, whose {@link Player} is player. This method is
     * useful in order to retrieve all the pieces on a player on the board, for exemple to know what pieces can be
     * moved on a player's turn.
     * <p>
     * This method also exist in static version ({@link  Board#getPiecesByPlayer(Set, Player, Class)}) in order to be able to
     * retrieve with multiple conditions by linking with getAllPieces and getPiecesByState.
     *
     * @param player All the pieces returned will be player's one.
     * @return {@link Set} of all the player's pieces on the board, even "dead" ones.
     */
    protected Set<P> getPiecesByPlayer(Player player){
        return this.pieces
                .stream()
                .filter(x -> x.getPlayer() == player)
                .collect(Collectors.toSet());
    }

    /**
     * Return a {@link Set} of all the pieces on the {@link Board} board, whose {@link Player} is player. This
     * method is useful in order to retrieve all the pieces on a player on the board, for exemple to know what pieces
     * can be moved on a player's turn.
     * <p>
     * This method also exist in non-static version ({@link Board#getPiecesByPlayer(Player)}).
     * The static version has to be overridden but can use this version with a binding for P.
     *
     * @param <P> Child class of {@link Piece} - type of the pieces on the Board
     * @param pieces {@link Set} of all the pieces to check
     * @param player All the pieces returned will be player's one.
     * @param pieceType Here to differentiate signature from the children methods only
     * @return {@link Set} of all the player's {@link Piece}s on the board, even "dead" ones.
     */
    static protected <P extends Piece> Set<P> getPiecesByPlayer(Set<P> pieces, Player player, Class pieceType){
        return pieces
                .stream()
                .filter(x -> x.getPlayer() == player)
                .collect(Collectors.toSet());
    }


    /**
     * Return a {@link Set} of all the pieces on the board, with a given {@link Piece.PieceState}. This method is
     * useful in order to retrieve all the pieces within a given state, for exemple to know what pieces are still alive
     * on the board.
     * <p>
     * This method also exist in static version ({@link  Board#getPiecesByState(Set, Piece.PieceState, Class)}) in order to be
     * able to retrieve with multiple conditions by linking with getAllPieces and getPiecesByPlayer.
     *
     * @param state All the pieces returned will have this state.
     * @return {@link Set} of all the pieces on the board within the given state.
     */
    protected Set<P> getPiecesByState(Piece.PieceState state) {
        return this.pieces
                .stream()
                .filter(x -> x.getState() == state)
                .collect(Collectors.toSet());
    }

    /**
     * Return a {@link Set} of all the pieces on the {@link Board} board, with a given {@link Piece.PieceState}.
     * This method is useful in order to retrieve all the pieces within a given state, for exemple to know what pieces
     * are still alive on the board.
     * <p>
     * This method also exist in non-static version ({@link Board#getPiecesByState(Piece.PieceState)}).
     * The static version has to be overridden but can use this version with a binding for P.
     *
     * @param <P> Child class of {@link Piece} - type of the pieces on the Board
     * @param pieces {@link Set} of all the pieces to check
     * @param state All the pieces returned will have this state.
     * @param pieceType Here to differentiate signature from the children methods only
     * @return {@link Set} of all the pieces on the board within the given state.
     */
    static protected <P extends Piece> Set<P> getPiecesByState(Set<P> pieces, Piece.PieceState state, Class pieceType) {
        return pieces
                .stream()
                .filter(x -> x.getState() == state)
                .collect(Collectors.toSet());
    }

    /**
     * Tell if the position pos is in the bounds of the board.
     * @param pos {@link Pos} to check
     * @return true if pos is on the board, false elsewhere
     */
    public boolean isOnBoard(Pos pos) {
        return (0 < pos.getY() && pos.getY() <= this.height &&
                0 < pos.getX() && pos.getX() <= this.width);
    }

    /**
     * Clear (empty) the board. This method can be used when restarting a game.
     */
    public void clear() {
        this.pieces.clear();
    }

    @Override
    public String toString() {
        return "Board{" +
                "width=" + width +
                ", height=" + height +
                '}';
    }
}
