package org.theGo.players;

import org.theGo.game.Color;
import org.theGo.game.GoBoard;
import org.theGo.game.Move;

/**
 * Represents a player in the game of Go.
 */
public abstract class GoPlayer {
    /**
     * The color of the player.
     */
    final Color color;
    /**
     * The nickname of the player.
     */
    String nickname;

    /**
     * Creates a new player with the given color.
     *
     * @param color the color of the player
     */
    public GoPlayer(Color color) {
        this.color = color;
    }

    /**
     * Asks the player to make a move.
     *
     * @param board the board on which the move is to be made
     * @return the move made by the player
     */
    public abstract Move takeTurn(GoBoard board);

    /**
     * Returns the name of the player.
     *
     * @return the name of the player
     */
    public abstract String getName();

    /**
     * Asks the player if they want to finish the game.
     *
     * @return true if the player wants to finish the game, false otherwise
     */
    public abstract boolean askFinish();

    /**
     * Displays a message to the player.
     *
     * @param message the message to be displayed
     */
    public abstract void message(String message);

    /**
     * Displays an error message to the player.
     *
     * @param message the error message to be displayed
     */
    public abstract void error(String message);

    /**
     * Returns the color of the player.
     *
     * @return the color of the player
     */
    public Color getColor() {
        return color;
    }

    /**
     * Returns the nickname of the player.
     *
     * @return the nickname of the player
     */
    public String getNickname() {
        return nickname;
    }
}
