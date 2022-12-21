package com.thibclnt.chessgame;


/**
 * Interface that must be implemented to communicate between {@link Application} functionalities and the user.
 */
public interface AppInterface {

    /**
     * Initialise (draw for the first time) the 'interface'.
     */
    void init();

    /**
     * @return The {@link Application} with which this interface allows to communicate.
     */
    Application getApplication();

    /**
     * Set the {@link Application} with which this interface allows to communicate.
     * @param application {@link Application} with which this interface allows to communicate.
     */
    void setApplication(Application application);
}
