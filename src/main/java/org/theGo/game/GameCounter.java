package org.theGo.game;

/**
 * Simple class for counting captured stones.
 */
public class GameCounter {
    private int blackStones = 0;

    private int whiteStones = 0;

    /**
     * Adds a captured stone to the counter.
     *
     * @param stoneColor color of the captured stone
     */
    public void addCapturedStone(Color stoneColor) {
        if (stoneColor == Color.BLACK) {
            blackStones += 1;
        } else {
            whiteStones += 1;
        }
    }

    /**
     * Returns the number of captured black stones.
     *
     * @return number of captured stones
     */
    public int getBlackKilled() {
        return blackStones;
    }

    /**
     * Returns the number of captured white stones.
     *
     * @return number of captured stones
     */

    public int getWhiteKilled() {
        return whiteStones;
    }
}
