package org.theGo.game;

public class Move {
    /**
     * Type of move: PASS, RESIGN, MOVE
     */
    public enum Type {
        PASS,
        RESIGN,
        MOVE
    }

    /**
     * Color that made the move: BLACK, WHITE
     */
    private final Color color;
    /**
     * Type of move: PASS, RESIGN, MOVE
     */
    private final Type type;
    /**
     * X coordinate of the move
     */
    private final int x;
    /**
     * Y coordinate of the move
     */
    private final int y;

    /**
     * Constructor for Move class
     *
     * @param color Color that made the move: BLACK, WHITE
     * @param type  Type of move: PASS, RESIGN, MOVE
     * @param x     X coordinate of the move
     * @param y     Y coordinate of the move
     */
    public Move(Color color, Type type, int x, int y) {
        this.color = color;
        this.type = type;
        this.x = x;
        this.y = y;
    }

    /**
     * Constructor for PASS and RESIGN moves, it sets x and y to -1;
     *
     * @param color Color that made the move: BLACK, WHITE
     * @param type  Type of move: PASS, RESIGN
     */
    public Move(Color color, Type type) {
        this.color = color;
        this.type = type;
        this.x = -1;
        this.y = -1;
    }

    /**
     * Checks if the move is of given type
     *
     * @param type Type to check
     * @return true if the move is of given type, false otherwise
     */
    public boolean isType(Type type) {
        return this.type == type;
    }

    /**
     * Returns x coordinate of the move
     *
     * @return x coordinate of the move
     */
    public int getX() {
        return x;
    }

    /**
     * Returns y coordinate of the move
     *
     * @return y coordinate of the move
     */
    public int getY() {
        return y;
    }

    /**
     * Returns color that made the move
     *
     * @return color that made the move
     */
    public Color getColor() {
        return color;
    }

    /**
     * Returns string representation of the move
     *
     * @return string representation of the move
     */
    public String toString() {
        String output = color.name() + " " + type.name();
        if (x != -1 && y != -1) {
            output += " " + x + " " + y;
        }
        return output;
    }
}
