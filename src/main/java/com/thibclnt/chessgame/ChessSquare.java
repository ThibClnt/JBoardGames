package com.thibclnt.chessgame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class allows me to have class which behaves like a button component, thanks to the inherits
 * from {@link JButton}.This personalized button has also an {@link ChessSquare#actionPerformed(ActionEvent)} function to
 * do actions in case of a click on the {@link ChessSquare} button.
 * <p>
 * Each square of the chess board is an object of this Class, each square has coordinates {@link ChessSquare#coordX} and {@link ChessSquare#coordY},
 * and to access the public function of the {@link ChessGui} class this object has the chess GUI in his private variables :
 * {@link ChessSquare#chessGui}
 */
public class ChessSquare extends JButton implements ActionListener {

    private final int width = 80;
    private final int heigth = 80;
    private final int coordX;
    private final int coordY;
    private final ChessGui chessGui;

    /**
     * will Create a Square on the chess board at {@link ChessSquare#coordX} and {@link ChessSquare#coordY} in the {@link ChessSquare#coordX}
     * GUI.
     * @param coordX Square's X coordinate on the chess board
     * @param coordY Square's Y coordinate on the chess board
     * @param chessGui  The GUI of the chess game.
     */
    public ChessSquare(int coordX, int coordY, ChessGui chessGui) {

        this.chessGui = chessGui;
        this.coordX = coordX;
        this.coordY = coordY;

        this.setSize(width, heigth);
        this.setBorderPainted(false);
        this.setOpaque(false);
        this.setContentAreaFilled(false);
        this.setIcon(new ImageIcon(""));
        this.addActionListener(this);
    }

    /**
     * this function will return the square's position (Button's position) in the chess board.
     * @return square's position in the chess board
     */
    public Pos getPos(){
        return new Pos(this.coordX, this.coordY);
    }


    /**
     * When a square is pressed, something will occur depending on the situation.
     * If a piece his now selected : sends {@link ChessGame#posChosen(Pos)} and set the pendingSelection to true variable thanks to this setter
     * {@link ChessGui#setPendingSelection(boolean)} to say that a piece is selected and possible moves are drawn on the board
     * Or if a piece kill or move, sends : {@link ChessGame#confirmPos(Pos)} which represents the new position of the piece selected.
     * This function is also responsible for refreshing the movement's historic {@link ChessGui#refreshHistorique}.
     * @param e will represent the action [a click], this action will have an object, the class of the object, his name etc ...
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        String iconOfTheSquare = this.getIcon().toString();

        if (iconOfTheSquare.equals("")){
            chessGui.setPendingSelection(false);
            chessGui.draw();

        }else{
            if (chessGui.getPendingSelection()) {

                if (iconOfTheSquare.substring(iconOfTheSquare.length() - 8, iconOfTheSquare.length() - 4).equals("KILL") || iconOfTheSquare.equals("Assets/LegalMove.png")) {
                    chessGui.refreshPlayersData();

                    //Si une pièce est Tuée
                    if (!iconOfTheSquare.equals("Assets/LegalMove.png")){
                        chessGui.refreshHistorique(chessGui.chessGame.getLastPieceTouched(), chessGui.chessGame.getBoard().getPieceAt(getPos()), getPos());
                    }else{
                        chessGui.refreshHistorique(chessGui.chessGame.getLastPieceTouched(), null, getPos());
                    }
                    chessGui.chessGame.confirmPos(getPos());

                    chessGui.setPendingSelection(false);
                }else{
                    chessGui.draw();
                    chessGui.chessGame.posChosen(getPos());
                    chessGui.setPendingSelection(true);
                }
            }
            else {
                chessGui.chessGame.posChosen(getPos());
                chessGui.setPendingSelection(true);
            }
        }
    }

}