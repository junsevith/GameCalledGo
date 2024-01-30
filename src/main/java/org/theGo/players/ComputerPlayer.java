package org.theGo.players;

import org.theGo.game.Color;
import org.theGo.game.GoBoard;
import org.theGo.game.GoTile;
import org.theGo.game.Move;

import java.util.*;

/**
 * Represents a player in the game of Go that is controlled by the computer.
 */
public class ComputerPlayer extends GoPlayer {

    public ComputerPlayer(Color color) {
        super(color);
        nickname = "komputer_v1";
    }


    @Override
    public Move takeTurn(GoBoard board) {
        ArrayList<Set<Move>> sets = new ArrayList<>(Arrays.asList(
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>()
        ));

        GoTile[][] tiles = board.getBoard();
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                GoTile current = tiles[i][j];
                if (current.getStoneColor() == null) {
                    //wszystkie puste miejsca
                    sets.getFirst().add(new Move(super.color, Move.Type.MOVE, i + 1, j + 1));
                    if (current.getNeighbors(color).isEmpty() && current.getNeighbors(color.opposite()).isEmpty()) {
                        //puste miejsca otoczone przez 4 puste kamienie
                        sets.get(1).add(new Move(super.color, Move.Type.MOVE, i + 1, j + 1));
                    }
                    int same = current.getNeighbors(color).size();
                    if (same > 0 && same < 4) {
                        if (same == 1) {
                            //puste miejsca połączone z 1 kamieniem tego samego koloru
                            sets.get(2).add(new Move(super.color, Move.Type.MOVE, i + 1, j + 1));

                            for (GoTile tile : current.getNeighborsTiles(null)) {
                                if (!tile.getNeighborsTiles(color.opposite()).isEmpty()) {
                                    //puste miejsca połączone z 1 kamieniem tego samego koloru, oddalone o 1 pole od kamienia przeciwnego koloru
                                    sets.get(3).add(new Move(super.color, Move.Type.MOVE, i + 1, j + 1));
                                }
                            }
                        }
                        int opposite = current.getNeighbors(color.opposite()).size();
                        if (opposite > 0) {
                            //puste miejsca połączone z 1 kamieniem tego samego koloru i kamieniem przeciwnego koloru
                            sets.get(4).add(new Move(super.color, Move.Type.MOVE, i + 1, j + 1));
                            for (GoTile tile : current.getNeighborsTiles(color.opposite())) {
                                if (tile.onlyBreath(current)) {
                                    //puste miejsca, które zbiją kamień przeciwnego koloru
                                    sets.get(5).add(new Move(super.color, Move.Type.MOVE, i + 1, j + 1));
                                }
                            }
                        }
                    }
                }
            }
        }
        Move current = null;
        boolean found = false;
        for (int i = sets.size() - 1; i >= 0 && !found; i--) {
            Iterator<Move> tile = sets.get(i).iterator();
            if (!tile.hasNext()) {
                continue;
            }
            do {
                current = tile.next();
                found = board.placeStone(current);
            } while (!found && tile.hasNext());
        }
        if (!found) {
            current = new Move(super.color, Move.Type.PASS);
        }

        return current;
    }

    @Override
    public String getName() {
        if (super.color == Color.BLACK) {
            return "komputer grający czarnymi";
        } else {
            return "komputer grający białymi";
        }
    }

    @Override
    public boolean askFinish() {
        return true;
    }

    @Override
    public void message(String message) {

    }

    @Override
    public void error(String message) {

    }
}
