package com.thibclnt.chessgame;

import java.util.Set;

/**
 * Piece base class for ChessGame. It extends {@link Piece} and is inherited by {@link Bishop}, {@link King}, {@link Knight},
 * {@link Pawn}, {@link Queen} and {@link Rook}
 *
 * Plus the attributes of a {@link Piece}, a ChessPiece have a type that describes if the piece is a pawn, a rook, etc.
 * It also supplies a method to get the squares that the piece threatened.
 */
public abstract class ChessPiece extends Piece {

    /**
     * Enumeration used to describe the type of the ChessPiece, ie if it is a pawn, a rook, a knight, a king, a queen or
     * a bishop.
     * Each of these has an abbreviation (called 'letter') and a value (for counting the points of the players).
     * letter and value are accessible in read only via getters.
     * <ul>
     *     <li>PAWN:   letter=PWN,     value=1</li>
     *     <li>KNIGHT: letter=KGT,     value=3</li>
     *     <li>BISHOP: letter=BSH,     value=3</li>
     *     <li>ROOK:   letter=ROK,     value=5</li>
     *     <li>QUEEN:  letter=QEE,     value=9</li>
     *     <li>KING:   letter=KNG,     value=0 (NA)</li>
     * </ul>
     */
    public enum ChessPieceType {
        PAWN("PWN", 1), ROOK("ROK", 5), KNIGHT("KGT", 3),
        BISHOP("BSH", 3), QUEEN("QEE", 9), KING("KNG", 0);

        private final String letter;
        private final int value;

        ChessPieceType(String letter, int value){
            this.letter = letter;
            this.value = value;
        }

        public String getLetter(){
            return this.letter;
        }

        public int getValue() {
            return value;
        }
    }

    /** Type of the piece (bishop, knight, king, pawn, queen or rook). */
    private final ChessPieceType type;
    /** {@link ChessBoard} on which the piece is alive */
    protected final ChessBoard board;

    /**
     * By default, the piece state is set to ALIVE, unless an error is encountered.
     * In this case, a RuntimeException will be thrown and the state will be set to ERROR
     *
     * @param board The {@link Board} on which the piece is (when alive). Can't be null.
     * @param player The {@link Player} who 'owns' the piece. Can't be null.
     * @param pos The piece's position {@link Pos} on the board. Can be null.
     * @param type Type of the piece (see {@link ChessPieceType}
     */
    ChessPiece(ChessBoard board, Player player, Pos pos, ChessPieceType type) {
        super(board, player, pos);
        this.board = board;
        this.type = type;
    }

    /** @see com.thibclnt.chessgame.ChessPiece#type */
    public ChessPieceType getType() {
        return type;
    }

    /** @see com.thibclnt.chessgame.ChessPiece#board */
    @Override
    public ChessBoard getBoard() {
        return this.board;
    }

    @Override
    public Set<Pos> getLegalMoves() {
        return getLegalMoves(false);
    }

    /** This method is used in order to factorise {@link ChessPiece#getLegalMoves()} and {@link ChessPiece#getAttacked()}
     * code. Indeed, the only difference is that the first ignore the king while the second doesn't (it is impossible to
     * move over the king, but it is possible to attack it).
     *
     * @param ignoreKing true if the king has to be ignored.
     */
    protected abstract Set<Pos> getLegalMoves(boolean ignoreKing);

    /**
     * Returns the positions under attack of this piece as a {@link Set} of {@link Pos}.
     *
     * @return The position threatened by the piece.
     */
    public Set<Pos> getAttacked() {
        return getLegalMoves(true);
    }

    @Override
    public void setState(PieceState state) {
        super.setState(state);

        if (state == PieceState.DEAD) {
            this.player.getEnemy().setScore(this.player.getEnemy().getScore() + this.type.getValue());
        }
    }

    @Override
    public String toString() {
        String posStr = (pos == null) ? "NULL" : "(" + pos.getX() + ", " + pos.getY() + ")";

        return "{" +
                "type=" + type +
                ", state=" + state +
                ", pos=" + posStr +
                ", player=" + player.getName() +
                '}';
    }
}
