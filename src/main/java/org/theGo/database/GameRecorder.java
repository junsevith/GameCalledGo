package org.theGo.database;

import org.theGo.game.Color;
import org.theGo.game.Move;

import java.util.ArrayList;

/**
 * Class responsible for recording game moves and board states.
 */
public class GameRecorder {
    private final ArrayList<Move> moves = new ArrayList<>();

    private final ArrayList<Color[][]> boardStates = new ArrayList<>();

    /**
     * Records the provided state of the board.
     *
     * @param boardState the current state of the board
     */
    public void recordBoardState(Color[][] boardState) {
        boardStates.add(boardState);
    }

    /**
     * Records the provided move.
     *
     * @param move the move to be recorded
     */
    public void recordMove(Move move) {
        moves.add(move);
    }

    /**
     * Checks if last two moves were passes and returns true if so.
     *
     * @return true if last two moves were passes, false otherwise
     */
    public boolean gameHalted() {
        if (moves.size() < 2) {
            return false;
        } else {
            Move lastMove = moves.getLast();
            Move secondLastMove = moves.get(moves.size() - 2);
            return lastMove.isType(Move.Type.PASS) && secondLastMove.isType(Move.Type.PASS);
        }
    }

    /**
     * Returns list of recorded moves.
     *
     * @return the last move
     */
    public ArrayList<Move> getMoves() {
        return moves;
    }

    /**
     * Returns state of board at given move.
     * @param i move number
     * @return state of board at given move
     */
    public Color[][] getState(int i) {
        if (i > boardStates.size() - 1 || i < -boardStates.size()) {
            return new Color[0][0];
        }
        if (i >= 0) {
            return boardStates.get(i);
        } else {
            return boardStates.get(boardStates.size() + i);
        }
    }
}
