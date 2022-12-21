package com.thibclnt.chessgame;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Main class for the chessGame, linking and controlling the user interface and the model (Board, Pieces...) and
 * containing game relative information such as players, etc.
 *
 * @see Game
 */
public class ChessGame implements Game {

    /**
     * Struct-like class to store moves.
     * A move contains two positions {@link Pos}, the origin and the destination, and the {@link ChessPiece} concerned.
     */
    public static class Move {
        public Pos from;
        public Pos to;
        public ChessPiece piece;

        /**
         * Constructor for the move
         * @param piece {@link ChessPiece} concerned by the move
         * @param from Original {@link Pos}
         * @param to Final {@link Pos}
         */
        Move(ChessPiece piece, Pos from, Pos to) {
            this.piece = piece;
            this.from = new Pos(from);
            this.to = new Pos(to);
        }
    }

    private ChessPiece lastPieceTouched;
    private final ChessBoard board;
    private Player playerTurn;
    private final ChessGameInterface gameInterface;
    private final Player j1;
    private final Player j2;
    private Move lastMove = null;

    /** Map to get letters (for columns) from integer */
    public final static Map<Integer, String> intToLettersMap = Stream.of(
            new Object[][] {
                    { 1, "A" },
                    { 2, "B" },
                    { 3, "C" },
                    { 4, "D" },
                    { 5, "E" },
                    { 6, "F" },
                    { 7, "G" },
                    { 8, "H" },
            }).collect(Collectors.toMap(data -> (Integer) data[0], data -> (String) data[1]));

    /** Map to get integers pos from letters (for columns) */
    public final static Map<String, Integer> lettersToIntMap = intToLettersMap.entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

    /**
     * Create the chess game then starts it. As the game has to be created after the {@link ChessGameInterface}, it is recommended to
     * create it directly in the constructor of the interface, and to retrieve the game as follows :
     * {@code
     * ChessGame game = new ChessGameInterfaceImplementation().getGame()
     * }
     *
     * @param gameInterface {@link ChessGameInterface} to communicate with the game
     */
    public ChessGame(ChessGameInterface gameInterface) {
        // Initialisation of the game interface
        this.gameInterface = gameInterface;
        this.gameInterface.init();

        // Initialisation of board and players
        this.board = new ChessBoard(this);

        this.j1 = new Player("Joueur 1", Player.COLOR.WHITE);
        this.j1.setPlayedMoves(0);
        this.j1.setScore(0);

        this.j2 = new Player("Joueur 2", Player.COLOR.BLACK);
        this.j2.setPlayedMoves(0);
        this.j2.setScore(0);

        j1.setEnemy(j2);

        // Once the game is initialized, start it
        this.start();
    }

    /**
     * The {@link ChessBoard} contains all the {@link ChessPiece} used in the game, even the dead ones.
     *
     * @return The board of the game
     */
    public ChessBoard getBoard() {
        return this.board;
    }

    /**
     * The game interface is used to communicate with users with methods such as askForMoves, declareWinner...
     * @see ChessGameInterface
     * @see GameInterface
     *
     * @return The ChessGameInterface
     */
    public ChessGameInterface getGameInterface() {
        return gameInterface;
    }

    /**
     * @see Player
     * @return The white player
     */
    public Player getJ1() {
        return j1;
    }

    /**
     * @see Player
     * @return The black player
     */
    public Player getJ2() {
        return j2;
    }

    /**
     * Private function used in initialisation (or reinitialisation) to create all the pieces on the board.
     */
    private void createPieces() {
        // Add all the pieces of classic Chess
        for (int i = 1 ; i < 9 ; i ++) {
            new Pawn(board, j1, i, 2);
            new Pawn(board, j2, i, 7);
        }

        new Rook(board, j1, 1, 1);
        new Rook(board, j1, 8, 1);
        new Rook(board, j2, 1, 8);
        new Rook(board, j2, 8, 8);

        new Knight(board, j1, 2, 1);
        new Knight(board, j1, 7, 1);
        new Knight(board, j2, 2, 8);
        new Knight(board, j2, 7, 8);

        new Bishop(board, j1, 3, 1);
        new Bishop(board, j1, 6, 1);
        new Bishop(board, j2, 3, 8);
        new Bishop(board, j2, 6, 8);

        new King(board, j1, 5, 1);
        new Queen(board, j1, 4, 1);
        new King(board, j2, 5, 8);
        new Queen(board, j2, 4, 8);
    }

    /**
     * Start initialisation. It is not really a start since the loop may be created by the game interface
     */
    @Override
    public void start() {
        this.createPieces();
        this.playerTurn = j1;
    }

    /**
     * Useless here.
     * @see Game#stop()
     */
    @Override
    public void stop() {
    }

    /**
     * Reset the game (clear the board, recreate the pieces, reset score...)
     */
    @Override
    public void reset() {
        this.board.clear();
        this.j1.setScore(0);
        this.j2.setScore(0);
        this.j1.setPlayedMoves(0);
        this.j2.setPlayedMoves(0);

        this.start();
    }

    /**
     * @return {@link Player} whose turn to play is
     */
    @Override
    public Player getPlayerTurn() {
        return this.playerTurn;
    }

    /**
     * Check if there is a checkmate or a stalemate, and call the appropriate GameInterface method to announce it.
     * This method is called in {@link #confirmPos(Pos)} after each move.
     */
    @Override
    public void checkWinCondition() {
        if (this.board.isCheckMate(this.playerTurn.getEnemy())) {
            this.gameInterface.declareWinner(playerTurn);
        } else if (this.board.isStaleMate(this.playerTurn.getEnemy())) {
            this.gameInterface.declareNull();
        } else if (this.board.isInCheck(this.playerTurn.getEnemy())) {
            this.gameInterface.tellCheck();
        }
    }

    /**
     *  This method must be used by the game interface in its {@link ChessGameInterface#draw()} method, to know what
     *  pieces must be drawn.
     * @return A set of pieces to draw, i.e. who are alive
     */
    public Set<ChessPiece> getPiecesToDraw() {
        return this.board.getPiecesByState(Piece.PieceState.ALIVE);
    }

    /**
     * This method is called by the ChessGameInterface when a position on the board is chosen by the user, in {@link
     * ChessGameInterface#askForMove()}.<p> If the position is not valid, a move is asked again. Otherwise, legal moves are
     * displayed by calling {@link ChessGameInterface#displayLegalMove(Set)}</p>
     * @param pos The positon chosen by the user
     * @see Pos
     */
    public void posChosen(Pos pos) {
        // Check is the pos is valid, i.e. in the board and on a piece
        ChessPiece pieceAtPos = this.board.getPieceAt(pos);

        if (this.board.isOnBoard(pos) && pieceAtPos != null && pieceAtPos.getPlayer() == this.playerTurn) {
            // If the pos is valid, then display legalMoves and ask for where to move
            this.lastPieceTouched = pieceAtPos;
            this.gameInterface.displayLegalMove(pieceAtPos.getLegalMoves());

        } else {
            // If the pos is not valid, then ask again for what piece to move
            this.gameInterface.askForMove();
        }
    }

    /**
     * This method is called once a legal pos is entered by the user in the GameInterface. It then moves the piece, kill
     * another if necessary then check for checkmate or stalemate.
     * @see Pos
     * @param pos position where the piece must move
     */
    public void confirmPos(Pos pos) {
        this.lastMove = new Move(lastPieceTouched, lastPieceTouched.getPos(), pos);
        lastPieceTouched.move(pos);
        this.playerTurn.setPlayedMoves(playerTurn.getPlayedMoves() + 1);
        this.gameInterface.draw();
        this.checkWinCondition();
        this.playerTurn = this.playerTurn.getEnemy();
    }

    /**
     * The last piece touched by a player may sometimes be retrieved by the GameInterface, for example in order to create
     * a history.
     * @return The last piece touched
     */
    public ChessPiece getLastPieceTouched(){
        return this.lastPieceTouched;
    }

    /**
     * This method can be called in order to get the last {@link Move}. The last moved can be useful to create a history,
     * and is used by the pawn for checking for en-passant moves.
     *
     * @return The last move
     */
    public Move getLastMove() { return this.lastMove; }

    /**
     * Compute the score of a {@link Player}. The score is calculated depending on the pieces killed by the player, with the
     * following scale : PAWN=1; BISHOP=KNIGHT=3; ROOK=5; QUEEN=9
     *
     * @param player Player whose score is calculated
     * @return the score of the player
     */
    public int getScore(Player player)  {
        return player.getScore();
    }
}
