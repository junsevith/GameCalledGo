package org.theGo.players;

import org.theGo.game.Color;
import org.theGo.game.GoBoard;
import org.theGo.game.Move;
import org.theGo.communication.Communicator;

/**
 * Represents a player in the game of Go that is controlled by a human.
 */
public class HumanPlayer extends GoPlayer {
    Communicator comm;

    public HumanPlayer(Color color, Communicator communicator) {
        super(color);
        comm = communicator;
    }

    @Override
    public Move takeTurn(GoBoard board) {
        while (true) {
            Move output = comm.takeMove("Podaj współrzędne ruchu:", super.color);
            int x = output.getX();
            int y = output.getY();

            if (x < 1 || x > board.getSize() || y < 1 || y > board.getSize()) {
                comm.error("Wybrano pole poza wymiarami planszy");
                continue;
            }

            if (!board.placeStone(x, y, super.color)) {
                comm.error("Wybrano pole poza wymiarami planszy");
                continue;
            } else {
                output = new Move(super.color, Move.Type.MOVE, x, y);
            }
            return output;
        }

    }

    @Override
    public String getName() {
        String output = "";
        if (nickname != null) {
            output += nickname + " ";
        }

        if (super.color == Color.BLACK) {
            output += "grający czarnymi";
        } else {
            output += "grający białymi";
        }
        return output;
    }

    @Override
    public boolean askFinish() {
        return comm.confirm("Czy chcesz zakończyć grę?", false);
    }

    @Override
    public void message(String message) {
        comm.message(message);
    }

    @Override
    public void error(String message) {
        comm.error(message);
    }

    @Override
    public String getNickname() {
        if (nickname == null) {
            nickname = comm.ask(getName() + ", wpisz swój nick (pozwoli on ci na znalezienie zapisu gry w bazie danych):");
        }
        return nickname;
    }
}


