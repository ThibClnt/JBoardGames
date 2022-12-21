package com.thibclnt.chessgame;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Represents the board in a game of chess. A ChessBoard contains {@link ChessPiece} and inherits from {@link Board}.
 * The size of this board is 8x8.
 * <p>
 *     ChessBoard contains general methods to add, and get pieces (by state, player, position...) but also chess-specific
 *     method to check if there is stale-mate, chess-mate, to promote pieces, etc. ChessPieces are added
 *     to the board during their instanciation.
 * </p>
 * @see ChessGame
 * @see ChessPiece
 * @see Board
 */
public class ChessBoard extends Board<ChessPiece>{

    private final ChessGame game;

    /**
     * Constructor of the ChessBoard, which must be linked to a {@link ChessGame} game. This constructor is called
     * during the instanciation of a chess game.
     *
     * @param game Game whose board is ChessBoard
     */
    public ChessBoard(ChessGame game) {
        super(8, 8);
        this.game = game;
    }

    /**
     * Method to promote a {@link Pawn} to another {@link ChessPiece}. A Pawn has to be promoted when it reaches the
     * opposite side of the board. Valid promotions are from pawn to rook, knight, bishop or queen. There is no limits
     * on the number of these promoted types.
     *
     * @param pawn {@link Pawn} to promote.
     * @param pieceType Valid promotion type from the {@link ChessPiece.ChessPieceType} enum.
     */
    public void promote(Pawn pawn, ChessPiece.ChessPieceType pieceType){
        Pos pos = new Pos(pawn.getPos());

        // Promote pawn into piece
        pawn.setState(Piece.PieceState.PROMOTED);

        switch (pieceType) {
            case ROOK -> new Rook(this, pawn.getPlayer(), pos);
            case QUEEN -> new Queen(this, pawn.getPlayer(), pos);
            case BISHOP -> new Bishop(this, pawn.getPlayer(), pos);
            case KNIGHT -> new Knight(this, pawn.getPlayer(), pos);
            default -> throw new RuntimeException("Invalid piece type (" + pieceType + ") for promotion");
        }
    }

    /**
     * Returns if a {@link Player} is in check, i.e. if its {@link King} is under attack of player's enemy.
     *
     * @param player {@link Player} that could be in check
     * @return true if player is in check, false elsewhere
     */
    public boolean isInCheck(Player player){
        King king = (King) getPiecesByType(getPiecesByPlayer(player), ChessPiece.ChessPieceType.KING).toArray()[0];

        return isInCheck(king.getPos(), player.getEnemy());
    }

    /**
     * Returns if a {@link Pos} is under attack of a {@link Player} enemy. This method is used mainly for checking if
     * a move is legal for a {@link ChessPiece}.
     *
     * @param pos {@link Pos} of the square that could be under attack
     * @param enemy {@link Player} that could threaten the square
     * @return true if the pos is under attack, false elsewhere
     */
    public boolean isInCheck(Pos pos, Player enemy){
        Set<Pos> threatened = new HashSet<>();

        for (ChessPiece piece: getPiecesByState(getPiecesByPlayer(enemy), Piece.PieceState.ALIVE)) {
            threatened.addAll(piece.getAttacked());
        }

        return threatened.contains(pos);
    }

    /**
     * Returns if a {@link Player} is check-mate or not, i.e. if its {@link King} is under attack, can't move and can't be defended.
     * A player who is check-mate loses the game.
     *
     * @param player {@link Player} who could be check-mate
     * @return true if player is check-mate, false elsewhere
     */
    public boolean isCheckMate(Player player){
        // Checkmate → King attacked + no moves for king

        // Retrieve the king of the player
        King king = (King) ChessBoard.getPiecesByType(getPiecesByPlayer(player), ChessPiece.ChessPieceType.KING).toArray()[0];
        if (king == null) {
            throw new RuntimeException("King for player " + player.getName() + " not found.");
        }

        // Check if the king is in check
        boolean isKingInCheck = isInCheck(king.getPos(), player.getEnemy());

        // Check if any Piece can move (if king is in check, legal moves only cover the king or kill the enemy)
        // Check if a piece can move or not
        boolean cannotMove = king.getLegalMoves().isEmpty();
        for (ChessPiece piece: getPiecesByState(this.getPiecesByPlayer(player), Piece.PieceState.ALIVE)) {
            cannotMove = cannotMove && piece.getLegalMoves().isEmpty();
        }

        return cannotMove && isKingInCheck;
    }

    /**
     * Returns if a {@link Player} is still in check after moving a {@link ChessPiece} to a new {@link Pos}. This method
     * is used to eliminate some moves from the legal moves of a piece, in case the king is or could be in check.
     *
     * @param player Player in check
     * @param piece Piece moved
     * @param newPos new pos of the piece
     * @return true if the king is still in check
     */
    public boolean isStillInCheck(Player player, ChessPiece piece, Pos newPos) {
        boolean stillInCheck;
        ChessPiece pieceAtNew = getPieceAt(newPos);  // Piece at new pos, if there is one

        // Move the piece to newPos, check if in check, then move the piece back to its old pos
        Pos oldPos = piece.getPos();
        piece.setPos(newPos);
        King king = (King) getPiecesByType(getPiecesByPlayer(player), ChessPiece.ChessPieceType.KING).toArray()[0];

        // If there is a piece at new pos, disable it before checking for check and enable it back
        if (pieceAtNew != null) pieceAtNew.setState(Piece.PieceState.ERROR);
        stillInCheck = isInCheck(king.getPos(), player.getEnemy());
        if (pieceAtNew != null) {
            pieceAtNew.setState(Piece.PieceState.ALIVE);
            pieceAtNew.setPos(newPos);
        }

        piece.setPos(oldPos);

        return stillInCheck;
    }

    /**
     * Returns if a {@link Player} is stale-mate or not, i.e. if its {@link King} is not under attack and none of its
     * pieces can move. A player who is check-mate loses the game.
     *
     * @param player {@link Player} who could be stale-mate
     * @return true if player is stale-mate, false elsewhere
     */
    public boolean isStaleMate(Player player){
        // Stalemate : No Legal moves for all the player pieces + not in check

        // Retrieve the king of the player
        King king = (King) ChessBoard.getPiecesByType(getPiecesByPlayer(player), ChessPiece.ChessPieceType.KING).toArray()[0];
        if (king == null) {
            throw new RuntimeException("King for player " + player.getName() + " not found.");
        }

        // Check if a piece can move or not
        boolean cannotMove = king.getLegalMoves().isEmpty();
        for (ChessPiece piece: getPiecesByState(this.getPiecesByPlayer(player), Piece.PieceState.ALIVE)) {
            cannotMove = cannotMove && piece.getLegalMoves().isEmpty();
        }

        // Check if the king is in check
        boolean isKingInCheck = isInCheck(king.getPos(), player.getEnemy());

        return cannotMove && !isKingInCheck;
    }

    /**
     * Overrides {@link Board#getAllPieces()} but returns a set of {@link ChessPiece}s.
     * @return {@link Set} of all the {@link Piece}s on the board, even "dead" ones.
     */
    @Override
    public Set<ChessPiece> getAllPieces() {
        return super.getAllPieces();
    }

    /**
     * Overrides {@link Board#getPiecesByPlayer(Player)} but returns a set of {@link ChessPiece}s.
     * @return {@link Set} of all the player's pieces on the board, even "dead" ones.
     */
    @Override
    public Set<ChessPiece> getPiecesByPlayer(Player player){
        return super.getPiecesByPlayer(player);
    }

    /**
     * Return a {@link Set} of all the pieces on the {@link ChessBoard} board, whose {@link Player} is player. This
     * method is useful in order to retrieve all the pieces on a player on the board, for exemple to know what pieces
     * can be moved on a player's turn.
     * <p>
     * This method also exist in non-static version ({@link ChessBoard#getPiecesByPlayer(Player)}).
     *
     * @param pieces {@link Set} of all the pieces to check
     * @param player All the pieces returned will be player's one.
     * @return {@link Set} of all the player's {@link ChessPiece}s on the board, even "dead" ones.
     */
    static public Set<ChessPiece> getPiecesByPlayer(Set<ChessPiece> pieces, Player player){
        return Board.getPiecesByPlayer(pieces,player,ChessPiece.class);
    }

    /**
     * Overrides {@link Board#getPiecesByState(com.thibclnt.chessgame.Piece.PieceState)} but returns a set of {@link ChessPiece}s.
     * @return {@link Set} of all the pieces on the board within the given state.
     */
    @Override
    public Set<ChessPiece> getPiecesByState(Piece.PieceState state) {
        return super.getPiecesByState(state);
    }

    /**
     * Return a {@link Set} of all the pieces on the {@link ChessBoard} board, with a given {@link Piece.PieceState}.
     * This method is useful in order to retrieve all the pieces within a given state, for exemple to know what pieces
     * are still alive on the board.
     * <p>
     * This method also exist in non-static version ({@link ChessBoard#getPiecesByState(Piece.PieceState)}).
     *
     * @param pieces {@link Set} of all the pieces to check
     * @param state All the pieces returned will have this state.
     * @return {@link Set} of all the pieces on the board within the given state.
     */
    static public Set<ChessPiece> getPiecesByState(Set<ChessPiece> pieces, Piece.PieceState state) {
        return Board.getPiecesByState(pieces,state,ChessPiece.class);
    }

    /**
     * Return a {@link Set} of all the chessPiece of a given {@link com.thibclnt.chessgame.ChessPiece.ChessPieceType}.
     * This method is useful in order to retrieve all the pieces of a given type, for exemple to retrieve kings.
     * <p>
     * This method also exist in static version ({@link  ChessBoard#getPiecesByType(Set, com.thibclnt.chessgame.ChessPiece.ChessPieceType)})
     * in order to be able to retrieve with multiple conditions by linking with getAllPieces and getPiecesByPlayer.
     *
     * @param type All the pieces returned will be of this type.
     * @return {@link Set} of all the pieces of type type on the board.
     */
    public Set<ChessPiece> getPiecesByType(ChessPiece.ChessPieceType type) {
        return pieces
                .stream()
                .filter(chessPiece -> chessPiece.getType() == type)
                .collect(Collectors.toSet());
    }

    /**
     * Return a {@link Set} of all the chessPiece of a given {@link com.thibclnt.chessgame.ChessPiece.ChessPieceType}.
     * This method is useful in order to retrieve all the pieces of a given type, for exemple to retrieve kings.
     * <p>
     * This method also exist in non-static version ({@link ChessBoard#getPiecesByType(com.thibclnt.chessgame.ChessPiece.ChessPieceType)}).
     *
     * @param pieces {@link Set} of all the pieces to check
     * @param type All the pieces returned will be of this type.
     * @return {@link Set} of all the pieces of type type on the board.
     */
    static public Set<ChessPiece> getPiecesByType(Set<ChessPiece> pieces, ChessPiece.ChessPieceType type) {
        return pieces
                .stream()
                .filter(chessPiece -> chessPiece.getType() == type)
                .collect(Collectors.toSet());
    }

    /**
     * @return The {@link ChessGame} linked to this board.
     */
    public ChessGame getGame() { return this.game; }
}
