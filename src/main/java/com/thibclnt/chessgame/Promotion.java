package com.thibclnt.chessgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Once a pawn is about to be promoted, this Class is called in the {@link ChessGui#askForPromotion} function.
 * It inherits from the {@link ActionListener} Class, so that any clicks on the inside buttons will be treated.
 * These buttons represent the four pieces a pawn could be promoted into, the Queen, the Knight, the Rook or the Bishop.
 * While no button is clicked, players can't move any pieces, and once the pawn is promoted the game can continue.
 */
public class Promotion implements ActionListener{
    private int x,y;
    private JDialog promotionPopUp;
    private String playerColor;
    private ChessGui myChessGui;
    private JButton buttonRook;
    private JButton buttonKnight;
    private JButton buttonBishop;
    private JButton buttonQueen;

    /**
     * This constructor of the {@link Promotion} Class displays a PopUp in the middle of the chess board, waiting to
     * a player choice. {@link Promotion#myChessGui} variable is used by {@link Promotion#actionPerformed} once a button is clicked.
     * @param myChessGui Is the Chess's GUI, the place where this PopOp will be displayed.
     */
    public Promotion(ChessGui myChessGui){

        this.myChessGui = myChessGui;

        JFrame frame = null;    // to fool compiler to allow null as first arg
        this.promotionPopUp = new JDialog(frame, "", true);

        JLabel labelPieces = new JLabel();
        labelPieces.setLayout(new GridLayout(1,4));

        if(this.myChessGui.chessGame.getPlayerTurn().getColor().equals(Player.COLOR.WHITE)) {
            this.playerColor = "WHITE";
        }else {
            this.playerColor = "BLACK";
        }

        this.buttonRook = new JButton();
        this.buttonRook.setBorderPainted(false);
        this.buttonRook.setSelected(false);
        this.buttonRook.setOpaque(false);
        this.buttonRook.setContentAreaFilled(false);
        this.buttonRook.addActionListener(this);
        this.buttonRook.setIcon(new ImageIcon("Assets/ROK_"+this.playerColor+".png"));

        this.buttonKnight = new JButton();
        this.buttonKnight.setSelected(false);
        this.buttonKnight.setBorderPainted(false);
        this.buttonKnight.setOpaque(false);
        this.buttonKnight.setContentAreaFilled(false);
        this.buttonKnight.addActionListener(this);
        this.buttonKnight.setIcon(new ImageIcon("Assets/KGT_"+this.playerColor+".png"));

        this.buttonBishop = new JButton();
        this.buttonBishop.setSelected(false);
        this.buttonBishop.setBorderPainted(false);
        this.buttonBishop.setOpaque(false);
        this.buttonBishop.setContentAreaFilled(false);
        this.buttonBishop.addActionListener(this);
        this.buttonBishop.setIcon(new ImageIcon("Assets/BSH_"+this.playerColor+".png"));

        this.buttonQueen = new JButton();
        this.buttonQueen.setSelected(false);
        this.buttonQueen.setBorderPainted(false);
        this.buttonQueen.setOpaque(false);
        this.buttonQueen.setContentAreaFilled(false);
        this.buttonQueen.addActionListener(this);
        this.buttonQueen.setIcon(new ImageIcon("Assets/QEE_"+this.playerColor+".png"));

        labelPieces.add(this.buttonRook);
        labelPieces.add(this.buttonKnight);
        labelPieces.add(this.buttonBishop);
        labelPieces.add(this.buttonQueen);

        this.promotionPopUp.add(labelPieces);

        this.promotionPopUp.setSize(500,100);
        this.x = myChessGui.getX()+(int)myChessGui.chessBoard.getLocation().getX()+((int)myChessGui.chessBoard.getWidth()/2)-(this.promotionPopUp.getWidth()/2);
        this.y = myChessGui.getY()+(int)myChessGui.chessBoard.getLocation().getY()+((int)myChessGui.chessBoard.getHeight()/2);

        this.promotionPopUp.setLocation(this.x,this.y);
        this.promotionPopUp.setUndecorated(true);
        this.promotionPopUp.setBackground(Color.decode("#e3e3e3"));
        this.promotionPopUp.setVisible(true);            // waits here until user closes window
    }


    /**
     * When the player has made his choice about which piece my pawn will promote into, the {@link ChessGui#setPieceChosenByPromotion}
     * function is called to save the player's choice. After that the saving the PopUp will disappear.
     * @param e will represent the action [a click], this action will have an object, the class of the object, his name etc ...
     */
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == this.buttonRook){
            this.myChessGui.setPieceChosenByPromotion(ChessPiece.ChessPieceType.ROOK);
            this.promotionPopUp.dispose();
        }
        if (e.getSource() == this.buttonKnight){
            this.myChessGui.setPieceChosenByPromotion(ChessPiece.ChessPieceType.KNIGHT);
            this.promotionPopUp.dispose();
        }
        if (e.getSource() == this.buttonBishop){
            this.myChessGui.setPieceChosenByPromotion(ChessPiece.ChessPieceType.BISHOP);
            this.promotionPopUp.dispose();
        }
        if (e.getSource() == this.buttonQueen){
            this.myChessGui.setPieceChosenByPromotion(ChessPiece.ChessPieceType.QUEEN);
            this.promotionPopUp.dispose();
        }
    }

}
