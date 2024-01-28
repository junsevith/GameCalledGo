package org.theGo.game;

import java.util.HashSet;
import java.util.Set;

/**
 * Board class for the Go game.
 */
public class GoBoard {
    /**
     * Array of tiles representing the board.
     */
    private final GoTile[][] board = new GoTile[19][19];

    /**
     * Size of the board.
     */
    private final int boardSize;

    /**
     * Counter for captured stones.
     */
    private final GameCounter counter = new GameCounter();

    /**
     * Creates a new board of given size.
     *
     * @param boardSize size of the board
     */
    public GoBoard(int boardSize) {
        this.boardSize = boardSize;
        setNeighbors();
    }

    /**
     * Returns the counter for captured stones.
     *
     * @return counter for captured stones
     */
    public GameCounter getCounter() {
        return counter;
    }

    /**
     * Sets the neighbors for each tile on the board.
     */
    private void setNeighbors() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = new GoTile(counter);
            }
        }

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                GoTile[] neighbors = new GoTile[4];
                if (i > 0) {
                    neighbors[0] = board[i - 1][j];
                }
                if (i < 18) {
                    neighbors[1] = board[i + 1][j];
                }
                if (j > 0) {
                    neighbors[2] = board[i][j - 1];
                }
                if (j < 18) {
                    neighbors[3] = board[i][j + 1];
                }
                board[i][j].setNeighbors(neighbors);
            }
        }
    }

    /**
     * Places a stone on the board.
     *
     * @param x     x coordinate of the stone
     * @param y     y coordinate of the stone
     * @param color color of the stone
     * @return true if the stone was placed, false otherwise
     */
    public boolean placeStone(int x, int y, Color color) {
        return board[x - 1][y - 1].placeStone(color);
    }

    /**
     * Returns the state of the board as a 2D array of colors.
     * Black stones are represented by Color.BLACK, white stones by Color.WHITE,
     * and empty tiles by null.
     *
     * @return state of the board
     */
    public Color[][] getState() {
        Color[][] state = new Color[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                state[i][j] = board[i][j].getStoneColor();
            }
        }
        return state;
    }

    /**
     * Returns the size of the board.
     *
     * @return size of the board
     */
    public int getSize() {
        return boardSize;
    }

    /**
     * Counts the territory of a given color, for counting the score.
     *
     * @param color color of the player
     * @return number of tiles in the territory
     */
    public int countTerritory(Color color) {
        Set<GoTile> already = new HashSet<>();
        Set<GoTile> territory = new HashSet<>();

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                Set<GoTile> temp = new HashSet<>();
                GoTile tile = board[i][j];
                if (tile.getStoneColor() == null && !already.contains(tile)) {
                    if (board[i][j].countTerritory(color, temp)) {
                        territory.addAll(temp);
                    }
                    already.addAll(temp);
                }
            }
        }

        return territory.size();
    }
}
