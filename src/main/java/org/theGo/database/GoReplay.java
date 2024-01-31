package org.theGo.database;

import org.theGo.communication.Communicator;
import org.theGo.game.GoBoard;
import org.theGo.game.Move;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class responsible for replaying games got from database
 */
public class GoReplay {
    private final Communicator comm;
    /**
     * List of moves that are to be replayed
     */
    private final ArrayList<Move> moves;
    /**
     * Flag that indicates if replay is still going
     */
    private boolean going = true;
    /**
     * Index of current move
     */
    private int currentMove = 0;
    /**
     * Board that is being displayed
     */
    private GoBoard board;

    /**
     * List of commands that can be used in this mode (for displaying purposes).
     */
    private final List<String> commands = List.of(
            "help",
            "prev",
            "next",
            "quit"
    );

    /**
     * Map of commands that can be used in this mode (for executing purposes).
     */
    private final Map<String, Runnable> commandSet = Map.of(
            "h", this::help,
            "help", this::help,
            "p", this::prevMove,
            "prev", this::prevMove,
            "n", this::nextMove,
            "next", this::nextMove,
            "q", this::exit,
            "quit", this::exit
    );

    /**
     * Creates new instance of GoReplay
     *
     * @param comm  communicator used for displaying and getting input
     * @param moves list of moves that are to be replayed
     * @param size  size of board
     */
    public GoReplay(Communicator comm, ArrayList<Move> moves, int size) {
        this.comm = comm;
        this.moves = moves;
        board = new GoBoard(size);
        start();
    }

    public void start() {
        displayBoard();
        while (going) {
            comm.choose("Podaj komendę: (h - pomoc)", commandSet, commands, 0, false).run();
        }
    }

    private void displayBoard() {
        String msg = "Ruch: " + currentMove + "/" + moves.size() + " ";
        if (currentMove > 0) {
            msg += " " + moves.get(currentMove - 1).toString();
        }
        comm.message(msg);
        comm.displayBoard(board);
    }

    private void help() {
        comm.message("""
                h, help - wyświetla pomoc
                p, prev - cofa się o jeden ruch
                n, next - idzie do przodu o jeden ruch
                1, quit - wyjście z trybu replay
                """);
    }

    private void prevMove() {
        if (currentMove > 0) {
            currentMove--;
            board = BoardFactory.recreate(board.getSize(), moves.subList(0, currentMove));
        }
        displayBoard();
    }

    private void nextMove() {
        if (currentMove < moves.size()) {
            Move move = moves.get(currentMove);
            if (move.isType(Move.Type.MOVE)) {
                board.placeStone(move.getX(), move.getY(), move.getColor());
            }
            currentMove++;
            displayBoard();
        } else {
            displayBoard();
            comm.message("Koniec gry");
        }

    }

    private void exit() {
        going = false;
    }
}
