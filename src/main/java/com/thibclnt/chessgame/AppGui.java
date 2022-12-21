package com.thibclnt.chessgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *  Creates the GUI that will be the selection menu of the available games.
 */
public class AppGui extends JFrame implements AppInterface, ActionListener {
    final int width = 1000;
    final int height = 800;
    private Application application = null;
    private JButton chessGameButton = new JButton("Chess");

    /**
     * Displays the menu to select a game.
     */
    @Override
    public void init() {

        setTitle("Application");

        chessGameButton.setText("Chess");
        chessGameButton.setSize(300, 100);
        //Center the button
        chessGameButton.setLocation((width/2)-(chessGameButton.getWidth()/2), height/2-(chessGameButton.getHeight()/2));


        //Add all the buttons
        add(chessGameButton);

        //Event listeners
        chessGameButton.addActionListener(this);

        setSize(width, height);
        getContentPane().setBackground( Color.decode("#8ac48f") );
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Simply return this application
     * @return An Application
     */
    @Override
    public Application getApplication() {
        return application;
    }

    /**
     * Sets an application.
     * @param application will be the new application
     */
    @Override
    public void setApplication(Application application) {
        this.application = application;
    }

    /**
     * In order to save the player's choice, each button which represent any possible game will have an action listener
     * from the {@link ActionListener} Class to get there response and call the correct GUI.
     * @param e will represent the action [a click], this action will have an object, the class of the object, his name etc ...
     */
    public void actionPerformed(ActionEvent e)
    {
        Game selected_game = null;

        if (e.getSource() == chessGameButton)
        {
            selected_game = new ChessGui().getGame();

            application.setCurrentGame(selected_game);

            System.out.println("\t\nPlay Chess\n\n");
            setVisible(false);
        }

    }
}
