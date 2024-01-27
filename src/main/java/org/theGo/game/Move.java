package org.theGo.game;

public class Move {
    public enum Type {
        PASS,
        RESIGN,
        MOVE
    }
    private final Color color;
    private final Type type;
    private final int x;
    private final int y;

    public Move(Color color, Type type, int x, int y) {
        this.color = color;
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public Move(Color color, Type type) {
        this.color = color;
        this.type = type;
        this.x = -1;
        this.y = -1;
    }

    public boolean isType(Type type) {
        return this.type == type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Color getColor() {
        return color;
    }
}
