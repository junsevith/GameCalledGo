package org.theGo.communication;

import org.theGo.game.GoBoard;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class Communicator {


    /**
     * Asks a question and returns the answer.
     *
     * @param question the question to ask
     * @return the answer
     */
    public abstract String ask(String question);

    /**
     * Asks a yes/no question and returns the answer as a boolean.
     * If the answer is not recognized, the question is repeated.
     *
     * @param question      the question to ask
     * @param defaultChoice the default choice (null if there is no default choice)
     * @return true if the answer is affirmative, false if negative
     */
    public abstract boolean confirm(String question, Boolean defaultChoice);

    /**
     * Asks to choose from a set of answers and returns the corresponding value from map.
     * If the answer is not recognized, the question is repeated.
     *
     * @param question      the question to ask
     * @param map           map of possible answers and corresponding values
     * @param options       list of possible answers (for displaying purposes)
     * @param defaultChoice the default choice (null if there is no default choice)
     * @return the answer
     */
    public abstract <T> T choose(String question, Map<String, T> map, List<String> options, Integer defaultChoice);

    /**
     * Asks to set a value and returns parsed value.
     * If the answer is not recognized, the question is repeated.
     *
     * @param question      the question to ask
     * @param parser        function to parse the answer
     * @param defaultChoice the default choice (null if there is no default choice)
     * @return value parsed from the answer
     */
    public abstract <T> T set(String question, Function<String, T> parser, T defaultChoice);

    /**
     * Prints a message.
     *
     * @param message the message to print
     */
    public abstract void message(String message);

    /**
     * Inform that the action was successful.
     *
     * @param message the message to print
     */
    public abstract void accept(String message);

    /**
     * Inform that the action was unsuccessful.
     *
     * @param message the message to print
     */
    public abstract void deny(String message);

    /**
     * Displays some text.
     *
     * @param message the message to display
     */
    public abstract void display(String message);

    /**
     * Displays the board.
     *
     * @param board the board to display
     */
    public abstract void displayBoard(GoBoard board);

    /**
     * Displays the score.
     *
     * @param blackPoints the number of black points
     * @param whitePoints the number of white points
     */
    public abstract void displayScore(int blackPoints, int whitePoints);
}
