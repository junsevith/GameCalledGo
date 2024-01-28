package org.theGo.game;

/**
 * Enum representing color of a stone, or player etc.
 */
public enum Color {
    BLACK, WHITE;

    /**
     * Returns opposite color
     *
     * @return opposite color
     */
    public Color opposite() {
        if (this == Color.BLACK) {
            return Color.WHITE;
        } else {
            return Color.BLACK;
        }
    }
}