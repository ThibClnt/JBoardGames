package com.thibclnt.chessgame;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

/**
 * ChessGui creates and initialize the chess GUI, inherits from {@link JFrame} so that this class behaves like so.
 * ChessGui implements {@link ChessGameInterface} to get the functionality to promote
 * pawns {@link ChessGameInterface#askForPromotion(Pawn)}, a specific feature of chess games. It also inherits from
 * {@link ActionListener} to create an action listener for this frame, which will be controlled by some control buttons,
 * pause the game, rematch or even quite the application.
 *
 * @see ChessGui#rematchButton
 * @see ChessGui#pauseButton
 * @see ChessGui#quitButton
 */
public class ChessGui extends JFrame implements ChessGameInterface, ActionListener {
    private boolean pendingSelection = false;
    private final JLabel internFrame = new JLabel();
    private ChessPiece.ChessPieceType pieceChosenByPromotion = null;
    ChessSquare[][] matricePlateau = new ChessSquare[8][8]; //Création d'une matrice de boutons, du 8x8 pour le plateau
    final private String maFont = "Serif"; //Police d'écriture
    final JLabel chessBoard = new JLabel();
    final private JLabel pionsLabel = new JLabel();
    final private JLabel nomJoueur1 = new JLabel("Joueur 1");
    final private JLabel legendeJoueur1 = new JLabel("<html>Score: 0<br>Coups jouer: 0<br>Pieces perdu: 0</html>");

    final private JLabel nomJoueur2 = new JLabel("Joueur 2");
    final private JLabel legendeJoueur2 = new JLabel("<html>Score: 0<br>Coups jouer: 0<br>Pieces perdu: 0</html>");
    final private JTextArea historiqueCoups = new JTextArea(20,20);
    final private JLabel controlButtons = new JLabel();
    final private JButton pauseButton = new JButton(" Pause ");
    final private JButton rematchButton = new JButton(" Rejouer ");
    final private JButton quitButton = new JButton(" Quitter ");
    final private JButton resumeButton = new JButton();
    final ChessGame chessGame;

    /**
     * Constructeur de la classe {@link ChessGui}
     */
    public ChessGui(){
        chessGame = new ChessGame(this);
        draw();
    }

    /**
     * Initialisation function of the {@link ChessGui} class, called from {@link ChessGame} class whenever the chess game is created.
     * It disposes all widgets in the GUI app frame, set widget's properties and action listeners if they have one.
     */
    public void init() {

        Dimension tailleEcran = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int hauteur = (int)tailleEcran.getHeight();
        int largeur = (int)tailleEcran.getWidth();

        setTitle("Chess Game");

        internFrame.setSize(1650,1000);
        internFrame.setLayout(null);
        internFrame.setLocation((int) (largeur*0.07), (int) (hauteur*0.05));

        //Partie gauche --------------------------------------
        //widgets concernant le joueur 1
        nomJoueur2.setBackground(Color.decode("#c3cfcc"));
        nomJoueur2.setOpaque(true);
        nomJoueur2.setLocation(20, 20);
        nomJoueur2.setSize(200, 50);
        nomJoueur2.setFont(new Font(maFont, Font.PLAIN, 30));
        nomJoueur2.setHorizontalAlignment(SwingConstants.CENTER);

        legendeJoueur2.setBackground(Color.decode("#ebebeb"));
        legendeJoueur2.setOpaque(true);
        legendeJoueur2.setLocation(20, 80);
        legendeJoueur2.setSize(200, 130);
        legendeJoueur2.setFont(new Font(maFont, Font.PLAIN, 25));
        legendeJoueur2.setVerticalAlignment(SwingConstants.TOP);


        //widgets concernant le joueur 2
        nomJoueur1.setBackground(Color.decode("#c3cfcc"));
        nomJoueur1.setOpaque(true);
        nomJoueur1.setLocation(20, 500);
        nomJoueur1.setSize(200, 50);
        nomJoueur1.setFont(new Font(maFont, Font.PLAIN, 30));
        nomJoueur1.setHorizontalAlignment(SwingConstants.CENTER);

        legendeJoueur1.setBackground(Color.decode("#ebebeb"));
        legendeJoueur1.setOpaque(true);
        legendeJoueur1.setLocation(20, 560);
        legendeJoueur1.setSize(200, 130);
        legendeJoueur1.setFont(new Font(maFont, Font.PLAIN, 25));
        legendeJoueur1.setVerticalAlignment(SwingConstants.TOP);
        //-------------------------------------------------


        //Le plateau de jeu--------------------------------------
        chessBoard.setLocation(240, 20);
        chessBoard.setOpaque(true);
        chessBoard.setIcon(new ImageIcon("Assets/board.png"));
        chessBoard.setSize(899, 899);
        //----------------------------------------------------

        //Pour les pions -------------------------------------
        pionsLabel.setLocation(283, 65);
        pionsLabel.setBackground(Color.GRAY);
        pionsLabel.setSize(810, 810);
        pionsLabel.setOpaque(false);
        pionsLabel.setLayout(new GridLayout(8, 8, 0, 0));

        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                matricePlateau[i][j] = new ChessSquare(i + 1, j + 1, this);
            }
        }

        //Ajout de tous les boutons dans le label de pions
        for (int i = 7; i >= 0; i--) {
            for (int j = 0; j <= 7; j++) {
                pionsLabel.add(matricePlateau[j][i]);
                //matrice_plateau[j][i].setText(String.valueOf("x :" + (matrice_plateau[j][i].coord_x) + ", y :" + (matrice_plateau[j][i].coord_y)));
            }
        }

        //----------------------------------------------------
        //Partie droite --------------------------------------

        //Historique des coups----------------------------
        JPanel pannel_coups = new JPanel();
        pannel_coups.setLocation(1150, 20);
        pannel_coups.setSize(450, 700);
        pannel_coups.setFont(new Font(maFont, Font.PLAIN, 25));
        pannel_coups.setBorder(new TitledBorder(new EtchedBorder(), "Historique"));

        historiqueCoups.setBackground(Color.decode("#CFCFCF"));
        historiqueCoups.setOpaque(true);
        historiqueCoups.setFont(new Font(maFont, Font.PLAIN, 25));
        historiqueCoups.setFocusable(false);

        JScrollPane scroll_barre = new JScrollPane(historiqueCoups);
        scroll_barre.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        pannel_coups.add(scroll_barre);
        //----------------------------

        //Boutons de contrôle sur la partie ---------------
        controlButtons.setLayout(new GridLayout(1,2));
        controlButtons.setLocation(1150, 750);
        controlButtons.setSize(450, 169);
        controlButtons.setFont(new Font(maFont, Font.PLAIN, 30));
        controlButtons.setVerticalAlignment(SwingConstants.TOP);
        controlButtons.setLayout(new GridLayout(2, 2));

        rematchButton.setFont(new Font(maFont, Font.PLAIN, 20));
        pauseButton.setFont(new Font(maFont, Font.PLAIN, 20));
        quitButton.setFont(new Font(maFont, Font.PLAIN, 20));

        rematchButton.addActionListener(this);
        pauseButton.addActionListener(this);
        quitButton.addActionListener(this);

        controlButtons.add(rematchButton);
        controlButtons.add(pauseButton);
        controlButtons.add(quitButton);
        //----------------------------

        //----------------------------------------------------


        //Ajout de tous les widgets du joueur 1
        internFrame.add(nomJoueur1);
        internFrame.add(legendeJoueur1);

        //Ajout de tous les widgets du joueur 2
        internFrame.add(nomJoueur2);
        internFrame.add(legendeJoueur2);

        //Ajout du chess board et des boutons par-dessus
        internFrame.add(pionsLabel);
        internFrame.add(chessBoard);

        //Ajout partie droite
        internFrame.add(pannel_coups);
        internFrame.add(controlButtons);

        add(internFrame);

        setLayout(null);
        getContentPane().setBackground(Color.decode("#95b798"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setVisible(true);
    }

    /**
     * Return if a piece is selected, that means piece's legal moves are drawn on the chess board.
     * @return Is there a selected piece
     */
    public boolean getPendingSelection(){
        return this.pendingSelection;
    }

    /**
     * Enables external calls to set the new value of pendingSelection for the state parameter
     * @see ChessGui#getPendingSelection()
     */
    public void setPendingSelection(boolean state){
        this.pendingSelection = state;
    }

    /**
     * Enables external calls to set the new value of {@link ChessGui#pieceChosenByPromotion} for the pieceChosenByPromotion parameter
     * This function is used by the JDialog which displays the promotion pieces for the user {Queen, Knight, Rook, Bishop}.
     * When clicked on the piece he wanted, call this setter to it.
     */
    public void setPieceChosenByPromotion(ChessPiece.ChessPieceType pieceChosenByPromotion) {
        this.pieceChosenByPromotion = pieceChosenByPromotion;
    }

    /**
     * Will refresh the board when there is any move, click or action with control buttons.
     * It refreshes the Board when a piece moved, refreshes also player's scores and possible moves of a clicked piece.
     *
     * @see ChessSquare#actionPerformed(ActionEvent)
     * @see ChessGame#confirmPos(Pos)
     */
    @Override
    public void draw() {

        Set<ChessPiece> pieces_to_draw = chessGame.getPiecesToDraw();
        int posX, posY = 0;
        String pieceName, pieceColor, completePiece = "";

        //Vider les pièces du plateau
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                matricePlateau[i][j].setIcon(new ImageIcon(""));
            }
        }

        for (ChessPiece piece : pieces_to_draw) {

            //Récupération des coordonnées
            posX = piece.getPos().getX();
            posY = piece.getPos().getY();

            //Récupération du type de pièce [Bishop, Knight ...] et de la couleur pour afficher l'image de la pièce correspondante
            pieceName = piece.getType().getLetter();
            pieceColor = piece.getPlayer().getColor().toString();
            completePiece = pieceName + "_" + pieceColor;

            //Ajout l'image de la pièce correspondante à ses coordonnées dans la matrice
            matricePlateau[posX-1][posY-1].setIcon(new ImageIcon("Assets/"+completePiece+".png"));
            matricePlateau[posX-1][posY-1].setBorder(BorderFactory.createLineBorder(Color.BLUE, 20));
        }

        refreshPlayersData();
    }

    /**
     * Will refresh (add new moves or washed them from the interface) the history of the game's movements.
     * @param pieceMoved Is the piece of type {@link ChessPiece} which has been moved.
     * @param pieceKilled Is the piece of type {@link ChessPiece} which has been killed.
     * @param newPosition Is the new position of the pieceMoved.
     */
    public void refreshHistorique(ChessPiece pieceMoved, ChessPiece pieceKilled, Pos newPosition){
        String move = pieceMoved.getType()+" "+ChessGame.intToLettersMap.get(pieceMoved.getPos().getX())+pieceMoved.getPos().getY();

        if (pieceKilled == null){
            move += " moved to ";
        }else {
            move += " killed "+pieceKilled.getType()+" ";
        }
        move += ChessGame.intToLettersMap.get(newPosition.getX())+newPosition.getY();
        historiqueCoups.setText(historiqueCoups.getText()+"\n"+move);
    }

    /**
     * Simply refreshes the data just under player's names, about theirs scores, loosed pieces and the movements count.
     */
    public void refreshPlayersData() {
        Player JoueurActuel = null;

        for (int i = 0; i < 2; i++) {
            if (i == 0){
                JoueurActuel = chessGame.getPlayerTurn();
            }
            else {
                JoueurActuel = chessGame.getPlayerTurn().getEnemy();
            }

            int NombrePieceAlive = 0;
            int score = this.chessGame.getScore(JoueurActuel);
            int coupJouer = JoueurActuel.getPlayedMoves();

            Set<ChessPiece> pieceJoueur = chessGame.getBoard().getPiecesByPlayer(JoueurActuel);

            //Calculer le nombre de pièces ALIVE que le joueur possède
            for (Piece piece : pieceJoueur) {
                if (piece.getState() == Piece.PieceState.ALIVE) {
                    NombrePieceAlive += 1;
                }
            }

            if (JoueurActuel.getColor() == Player.COLOR.WHITE) {
                legendeJoueur1.setText("<html>Score: " + score + "<br>Coups joués: " + coupJouer + "<br>Pieces perdues: " + (16 - NombrePieceAlive) + "</html>");
            }
            if (JoueurActuel.getColor() == Player.COLOR.BLACK) {
                legendeJoueur2.setText("<html>Score: " + score + "<br>Coups joués: " + coupJouer + "<br>Pieces perdues: " + (16 - NombrePieceAlive) + "</html>");
            }
        }
    }

    /**
     * Unused by the GUI app.
     * @see ChessGameInterface#askForMove()
     */
    @Override
    public void askForMove() {

    }

    /**
     * Ones the possible moves of a piece is received by the {@link ChessSquare} classe, it displays all the possible moves for a selected piece, either your movements with grey squares or
     * possible kills associated by a red square on the opponent's piece.
     * This function is called whenever a piece is clicked, in the {@link ChessSquare#actionPerformed(ActionEvent)} function.
     * @param legalMoves Will be a {@link Set} of {@link Pos} of the possible moves to display
     */
    public void displayLegalMove(Set<Pos> legalMoves){

        for (Pos move : legalMoves) {
            //Si il y a une pièce
            if (!matricePlateau[move.getX() - 1][move.getY() - 1].getIcon().toString().equals("")){

                String IconImage = matricePlateau[move.getX()-1][move.getY()-1].getIcon().toString();
                IconImage = IconImage.substring(0,IconImage.length()-4) + "_KILL.png";
                matricePlateau[move.getX()-1][move.getY()-1].setIcon(new ImageIcon(IconImage));
            }
            else{
                matricePlateau[move.getX()-1][move.getY()-1].setIcon(new ImageIcon("Assets/LegalMove.png"));
            }
        }
    }

    /**
     * When one of the kings is dead, displays the winner inside the movement's historic.
     * @param player Will return the winner
     */
    @Override
    public void declareWinner(Player player) {
        historiqueCoups.setText(historiqueCoups.getText() + "\n\t"+player.getName()+" WON");
    }

    /**
     *  When one of the kings is not able to do any moves, and he is note in check, declares PAT in the movement's historic.
     */
    @Override
    public void declareNull() {
        historiqueCoups.setText(historiqueCoups.getText() + "\n\n\t PAT");
    }

    /**
     * Whenever a pound is about to be promoted, a window will appear and the program will wait until the user send a response.
     * At the end the pieceChosenByPromotion will be reset to null.
     * @param pawn Will be the pawn which is about to be promoted
     * @return Piece choose to upgrade the pawn.
     */
    @Override
    public ChessPiece.ChessPieceType askForPromotion(Pawn pawn) {

        ChessPiece.ChessPieceType choosePiece;
        new Promotion(this);

        choosePiece = this.pieceChosenByPromotion;
        this.pieceChosenByPromotion = null;

        return choosePiece;
    }

    /**
     * Display if the player is in check in the movement's historic.
     */
    @Override
    public void tellCheck() {
        historiqueCoups.setText(historiqueCoups.getText()+"\n\tÉchec " + chessGame.getPlayerTurn().getEnemy().getName());
    }

    /**
     * Return the chess game of the chess GUI.
     * @return the chess game, {@link ChessGame}
     */
    public Game getGame() { return this.chessGame; }

    /**
     * this function in due the implementation of {@link ActionListener} class, it enables the GUI app to be control,
     * if needed, by three buttons, one to pause the game, another to rematch and a last to quite the application.
     * @param e will represent the action [a click], this action will have an object, the class of the object, his name etc ...
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.rematchButton){
            chessGame.reset();
            historiqueCoups.setText("");
            draw();
        }
        if (e.getSource() == this.pauseButton){

            JFrame frame = null;
            JDialog pausedPopUp = new JDialog(frame, "Game paused", true);
            pausedPopUp.setUndecorated(true);
            pausedPopUp.setBackground(Color.decode("#ebc07a"));
            pausedPopUp.setSize(200,200);

            int x =  (int)(getX()+chessBoard.getLocation().getX()+(chessBoard.getWidth()/2)-(int)(pausedPopUp.getWidth()/2));
            int y =  (int)(getY()+chessBoard.getLocation().getY()+(int)(chessBoard.getHeight()/2));

            pausedPopUp.setLocation(x,y);

            ActionListener clickListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() == resumeButton){
                        pausedPopUp.dispose();
                    }
                }
            };

            resumeButton.setIcon(new ImageIcon("Assets/Pause.png"));
            resumeButton.addActionListener(clickListener);

            pausedPopUp.add(resumeButton);
            pausedPopUp.setVisible(true);
        }
        if (e.getSource() == this.quitButton){
            System.exit(0);
        }
    }
}
