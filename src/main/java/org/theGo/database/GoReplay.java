package org.theGo.database;

import org.theGo.communication.Communicator;
import org.theGo.game.GoBoard;
import org.theGo.game.Move;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GoReplay {
    private final Communicator comm;
    private final ArrayList<Move> moves;
    private boolean going = true;
    private int currentMove = 0;
    GoBoard board;

    private final List<String> commands = List.of(
            "help",
            "prev",
            "next",
            "exit"
    );

    private final Map<String, Runnable> commandSet = Map.of(
            "h", this::help,
            "help", this::help,
            "p", this::prevMove,
            "prev", this::prevMove,
            "n", this::nextMove,
            "next", this::nextMove,
            "e", this::exit,
            "exit", this::exit
    );

    public GoReplay(Communicator comm, ArrayList<Move> moves, int size) {
        this.comm = comm;
        this.moves = moves;
        board = new GoBoard(size);
        start();
    }

    public void start() {
        displayBoard();
        while (going) {
            comm.choose("Podaj komendę: (h - pomoc)", commandSet, commands, 0).run();
        }
    }

    private void displayBoard() {
        comm.displayBoard(board);
        comm.message("Ruch: " + currentMove + "/" + moves.size());
        if (currentMove > 0){
            comm.message("Poprzedni ruch: " + moves.get(currentMove - 1).toString());
        }
    }

    private void help() {
        comm.message("""
                h, help - wyświetla pomoc
                p, prev - cofa się o jeden ruch
                n, next - idzie do przodu o jeden ruch
                e, exit - wyjście z trybu replay
                """);
    }

    private void prevMove() {
        if (currentMove > 0) {
            currentMove--;
            board = BoardFactory.recreate(board.getSize(), moves.subList(0, currentMove));
            displayBoard();
        }

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
            comm.message("Koniec gry");
        }

    }

    private void exit() {
        going = false;
    }
}
