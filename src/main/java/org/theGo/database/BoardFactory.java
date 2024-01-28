package org.theGo.database;

import org.theGo.game.GoBoard;
import org.theGo.game.Move;

import java.util.List;

/**
 * Class responsible for recreating board from list of moves.
 */
public class BoardFactory {
    /**
     * Recreates board from list of moves.
     *
     * @param size  size of board
     * @param moves list of moves
     * @return recreated board
     */
    public static GoBoard recreate(int size, List<Move> moves) {
        GoBoard board = new GoBoard(size);
        for (Move move : moves) {
            if (move.isType(Move.Type.MOVE)) {
                board.placeStone(move.getX(), move.getY(), move.getColor());
            }
        }
        return board;
    }
}
