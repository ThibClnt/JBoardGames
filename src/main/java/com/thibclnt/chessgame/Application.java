package com.thibclnt.chessgame;

/**
 * Entry point of the package. When creating an application, it is possible to choose what {@link AppInterface} to use.
 * The application also allow to choose, via its appInterface, what game to play.
 * An application must be created and launched like this :
 * <code> new Application(appInterface).init(); </code>
 */
public class Application {

    /**
     * The current game that is played. null if no game is currently played.
     */
    private Game currentGame = null;
    /**
     * The human machine interface to use, which must allow to choose the game to play.
     */
    private final AppInterface appInterface;

    /**
     * Construct the application and attach an {@link AppInterface} to it.
     * @param appInterface The human machine interface to use, which must allow to choose the game to play.
     */
    public Application(AppInterface appInterface) {
        this.appInterface = appInterface;
        this.appInterface.setApplication(this);
    }

    /**
     * Launch the application (call {@link AppInterface#init()}
     */
    public void init() {
        this.appInterface.init();
    }

    /**
     * @return The human machine interface used ({@link AppInterface})
     */
    public AppInterface getAppInterface() {
        return appInterface;
    }

    /**
     * @return The {@link Game} currently played, or null if no game is played
     */
    public Game getCurrentGame() {
        return currentGame;
    }

    /**
     * Change the {@link Game} currently played (stop the old one if it exists).
     * @param currentGame The {@link Game} to play
     */
    public void setCurrentGame(Game currentGame) {
        if (this.currentGame != null)
            currentGame.stop();
        this.currentGame = currentGame;
    }
}
