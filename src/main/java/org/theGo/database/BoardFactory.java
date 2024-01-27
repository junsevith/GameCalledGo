package org.theGo.database;

import org.theGo.game.GoBoard;
import org.theGo.game.Move;

import java.util.ArrayList;

public class BoardFactory {
    public static GoBoard recreate(int size, ArrayList<Move> moves) {
        GoBoard board = new GoBoard(size);
        for (Move move : moves) {
            if (move.isType(Move.Type.MOVE)) {
                board.placeStone(move.getX(), move.getY(), move.getColor());
            }
        }
        return board;
    }
}
