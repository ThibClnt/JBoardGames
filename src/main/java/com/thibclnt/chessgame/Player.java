package com.thibclnt.chessgame;

/**
 * Basic general class to represent a player in a board game.
 * A player has a name and a color, and can have a score, an enemy and a number of moves played.
 */
public class Player{
    /** Color of the player's pieces (BLACK or WHITE) */
    public enum COLOR{WHITE, BLACK}

    private String name;
    /**
     * Color of the player's pieces.
     * @see COLOR
     */
    private COLOR color;
    /**
     * Enemy of the player. Useful when checking if the enemy loses after a move, or if the enemy threaten the current's
     * player pieces, for example.
     * */
    private Player enemy;
    /**
     * Integer score of the player. The score is computed by the game logic itself (because it depends on the game rules).
     */
    private int score;
    /**
     * Number of moves done by the player. Can be used to evaluate the length of a game, for example.
     */
    private int playedMoves;

    /**
     * A player needs at list a name and a color.
     *
     * @param name {@link Player#name} of the player.
     * @param color {@link Player#color} of the player.
     */
    public Player(String name, COLOR color) {
        this.name = name;
        this.score = 0;
        this.color = color;
        this.playedMoves = 0;
    }

    /**
     * A player needs at list a name and a color. The score can be set if it sums up with other games and is reloaded
     * from a file, for example.
     *
     * @param name {@link Player#name} of the player.
     * @param color {@link Player#color} of the player.
     * @param score {@link Player#score} of the player.
     */
    public Player(String name, COLOR color, int score) {
        this.name = name;
        this.score = score;
        this.color = color;
        this.playedMoves = 0;
    }

    /**
     * @return {@link Player#name} of the player.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the color of a player. Useful if a rule is to switch color between each games, for example.
     * @param color New {@link Player#color} of the player.
     */
    public void setColor(COLOR color) {
        this.color = color;
    }

    /**
     * @return {@link Player#color} of the player.
     */
    public COLOR getColor() {
        return color;
    }

    /**
     * @return {@link Player#playedMoves} of the player.
     */
    public int getPlayedMoves() {
        return playedMoves;
    }

    /**
     * Useful to update the player's moves played count.
     * @param playedMoves New {@link Player#playedMoves} of the player.
     */
    public void setPlayedMoves(int playedMoves) {
        this.playedMoves = playedMoves;
    }

    /**
     * Set the name of a player. Useful if the name of the player can be configured during a game.
     * @param name New {@link Player#name} of the player.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return {@link Player#score} of the player.
     */
    public int getScore() {
        return score;
    }

    /**
     * Set the score of a player. Useful when the score of the player is recomputed, in order to update it.
     * @param score New {@link Player#score} of the player.
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Set the enemy of a player. In general, a 'white' player is enemy with a 'black' player. As the relationship has
     * to be reciprocal, this method also set the enemy's enemy to the current player.
     * @param enemy {@link Player#enemy} of the player.
     */
    public void setEnemy(Player enemy) {
        this.enemy = enemy;
        enemy.enemy = this;
    }

    /**
     * @return {@link Player#enemy} of the player.
     */
    public Player getEnemy() { return this.enemy; }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", score=" + score +
                "playedMoves="+ playedMoves +
                ", color=" + color +
                '}';
    }
}