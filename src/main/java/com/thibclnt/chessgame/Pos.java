package com.thibclnt.chessgame;


/**
 * Represents a square position on a board. It behaves basically like a 2D vector, with x and y as integer coordinates.
 */
public class Pos {
    /** Coordinate from left (1 is the most left position) to right */
    private int x;
    /** Coordinate from bottom (1 is the lowest position) to top */
    private int y;

    /**
     * Copy constructor for pos
     *
     * @param pos Pos to copy
     */
    public Pos(Pos pos) {
        this.x = pos.x;
        this.y = pos.y;
    }

    /**
     * Main constructor for pos - Construct with coordinates
     *
     * @param x Integer coordinate from left (1 is the most left position) to right
     * @param y Integer coordinate from bottom (1 is the lowest position) to top
     */
    public Pos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /** @see Pos#x */
    public int getX() {
        return x;
    }

    /** @see Pos#x */
    public void setX(int x) {
        this.x = x;
    }

    /** @see Pos#y */
    public int getY() {
        return y;
    }

    /** @see Pos#y */
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pos pos = (Pos) o;

        return x == pos.x && y == pos.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 1000 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return "Pos{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}