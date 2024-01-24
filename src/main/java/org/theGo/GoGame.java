package org.theGo;

import org.theGo.communication.Communicator;
import org.theGo.players.GoPlayer;

import java.util.Arrays;

public class GoGame {

    GoBoard board;
    GoPlayer black;
    GoPlayer white;

    GameRecorder recorder = new GameRecorder();

    Communicator broadcast;

    public GoGame(GoPlayer player1, GoPlayer player2, Communicator broadcast, int BoardSize) {
        if (player1.getColor() == Color.WHITE) {
            white = player1;
            black = player2;
        } else {
            white = player2;
            black = player1;
        }
        this.broadcast = broadcast;
        board = new GoBoard(BoardSize);
    }

    /**
     * Rozpoczyna grę
     */
    public void startGame() {
        broadcast.message("Witaj w grze GO!");
        beginGame();
        countPoints();
    }


    /**
     * Rozpoczyna grę
     */
    private void beginGame() {
        broadcast.message("Zaczynamy grę!");
        GoPlayer activePlayer = black;
        while (gameIsNotOver()) {
            broadcast.message("\033[H\033[2J");
            broadcast.message(board.printBoard());
            broadcast.message("Punkty czarnego: " + board.getCounter().getWhiteKilled());
            broadcast.message("Punkty białego: " + board.getCounter().getBlackKilled());
            broadcast.message("Teraz ruch wykonuje " + activePlayer.getName());

            while (true) {
                String move = activePlayer.takeTurn(board);

                if (!move.split(" ")[1].equals("pas") && Arrays.deepEquals(board.getState(), recorder.getState(-2))) {
                    activePlayer.message("Nie można wykonać ruchu ko");
                    board = GoBoard.generateBoard(board.getSize(), recorder.getMoveHistory());
                } else {
                    recorder.recordMove(move);
                    recorder.recordBoardState(board.getState());
                    break;
                }
            }

            if (activePlayer == black) {
                activePlayer = white;
            } else {
                activePlayer = black;
            }
        }
    }

    /**
     * Sprawdza, czy gra się nie skończyła
     *
     * @return true, jeśli gra się nie skończyła, false, jeśli tak
     */
    private boolean gameIsNotOver() {
        if (recorder.gameHalted()) {
            return !(black.askFinish() && white.askFinish());
        } else {
            return true;
        }
    }

    private void countPoints() {
        int blackPoints = board.getCounter().getWhiteKilled() + board.countTerritory(Color.BLACK);
        int whitePoints = board.getCounter().getBlackKilled() + board.countTerritory(Color.WHITE);
        broadcast.message("Punkty czarnego: " + blackPoints);
        broadcast.message("Punkty białego: " + whitePoints);
        if (blackPoints > whitePoints) {
            broadcast.message("Wygrał czarny!");
        } else if (whitePoints > blackPoints) {
            broadcast.message("Wygrał biały!");
        } else {
            broadcast.message("Remis!");
        }
    }
}
