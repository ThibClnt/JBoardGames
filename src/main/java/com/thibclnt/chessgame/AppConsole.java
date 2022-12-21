package com.thibclnt.chessgame;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Application in console to play the {@link Game} we chose to play.
 * Essentially, ask for the player which game to launch.
 * Do verify each step if input are correct.
 * @see ChessGame
 * @see ChessBoard
 */
public class AppConsole implements AppInterface{

    private final Scanner scan = new Scanner(System.in);
    private Application application = null;

    private List<Integer> possible_choices = Arrays.asList(1);


    /**
     * Initialization of {@link AppConsole} to be called right after creating the object.
     * Display a welcome message, followed by the question of which game to play.
     * Create the game asked and set {@link Application}'s game to the game chosen.
     */
    @Override
    public void init() {
        print_line("\t\t********************************************");
        print_line("\t\t* Bienvenue sur notre plateforme de jeux ! *");
        print_line("\t\t********************************************");

        print_line("Vous pouvez choisir de jouer a differents jeux entre la selection suivante :");
        int game_number = print_ask_games();

        Game game;
        switch (game_number){
            case 1:
                game = new ChessConsole().getGame();
                break;
            default:
                game = new ChessConsole().getGame();
                break;
        }
        this.application.setCurrentGame(game);


    }

    /**
     * System.out.println(String) method (simplify code lines)
     * @param s String : the string to print
     */
    public void print_line(String s){
        //Just to simplify printing
        System.out.println(s);
    }

    /**
     * Function that displays the games available to play, and chose by inputting an integer in console.
     * catch non-int inputs.
     * @return response : the number of the chosen game.
     */
    public int print_ask_games(){
        /*

         */
        boolean valid = false;
        int response = 0;
        while(!valid){
            //list of games
            print_line("1) Echec");
            try{
                //re asking if not valid response
                response = scan.nextInt();
                if  (this.possible_choices.contains(response)){
                    valid = true;
                }
                else {
                    print_line("Erreur, vous devez repondre par un nombre dans la liste suivante :");
                }
            }
            catch (InputMismatchException e) {
                print_line("Erreur, vous devez repondre par un nombre dans la liste suivante :");
                scan.next();
            }
        }
        return response;
    }

    /** @see Application */
    public Application getApplication() {
        return application;
    }

    /** @see Application */
    public void setApplication(Application application) {
        this.application = application;
    }
}
