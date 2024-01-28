package org.theGo.app;

import org.theGo.database.BoardFactory;
import org.theGo.database.Database;
import org.theGo.database.DatabaseHandler;
import org.theGo.game.Color;
import org.theGo.database.GameRecorder;
import org.theGo.game.GoBoard;
import org.theGo.communication.Communicator;
import org.theGo.game.Move;
import org.theGo.players.GoPlayer;

import java.sql.SQLException;
import java.util.Arrays;

/**
 * Aplikacja do gry w Go.
 */
public class GoGame {

    private GoBoard board;
    private final GoPlayer black;
    private final GoPlayer white;

    private final GameRecorder recorder = new GameRecorder();

    private final Communicator broadcast;

    public GoGame(final GoPlayer player1, final GoPlayer player2, final Communicator broadcast, final int boardSize) {
        if (player1.getColor() == Color.WHITE) {
            white = player1;
            black = player2;
        } else {
            white = player2;
            black = player1;
        }
        this.broadcast = broadcast;
        board = new GoBoard(boardSize);
    }

    /**
     * Uruchamia aplikację.
     */
    public void start() {
        beginGame();
        countPoints();
    }


    /**
     * Rozpoczyna grę.
     */
    private void beginGame() {
        broadcast.message("Zaczynamy grę!");
        GoPlayer activePlayer = black;

        while (gameIsNotOver()) {
            broadcast.displayBoard(board);
            broadcast.displayScore(board.getCounter().getBlackKilled(), board.getCounter().getWhiteKilled());
            broadcast.message("Teraz ruch wykonuje " + activePlayer.getName());

            Move move;
            while (true) {
                move = activePlayer.takeTurn(board);

                if (!move.isType(Move.Type.PASS) && Arrays.deepEquals(board.getState(), recorder.getState(-2))) {
                    activePlayer.error("Nie można wykonać ruchu ko");
                    board = BoardFactory.recreate(board.getSize(), recorder.getMoves());
                } else {
                    recorder.recordMove(move);
                    recorder.recordBoardState(board.getState());
                    break;
                }
            }

            if (move.isType(Move.Type.RESIGN)) {
                broadcast.message("Gracz " + activePlayer.getName() + " poddał grę");
                break;
            }

            if (activePlayer == black) {
                activePlayer = white;
            } else {
                activePlayer = black;
            }
        }
    }

    /**
     * Sprawdza, czy gra się nie skończyła.
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

    /**
     * Zlicza punkty i wyświetla wynik.
     */
    private void countPoints() {
        int blackPoints = board.getCounter().getWhiteKilled() + board.countTerritory(Color.BLACK);
        int whitePoints = board.getCounter().getBlackKilled() + board.countTerritory(Color.WHITE);
        broadcast.message("Punkty czarnego: " + blackPoints);
        broadcast.message("Punkty białego: " + whitePoints);
        Color winner;
        if (blackPoints > whitePoints) {
            broadcast.message("Wygrał czarny!");
            winner = black.getColor();
        } else if (whitePoints > blackPoints) {
            broadcast.message("Wygrał biały!");
            winner = white.getColor();
        } else {
            broadcast.message("Remis!");
            winner = null;
        }
        saveGame(winner);
    }

    /**
     * Zapisuje grę do bazy danych.
     *
     * @param winner kolor zwycięzcy
     */
    private void saveGame(final Color winner) {
        try {
            DatabaseHandler databaseHandler = new DatabaseHandler(Database.getInstance());

            String blackNick = black.getNickname();
            if (blackNick.isEmpty()) {
                blackNick = "player1";
            }
            String whiteNick = white.getNickname();
            if (whiteNick.isEmpty()) {
                whiteNick = "player2";
            }

            String winnerNick;
            if (winner == Color.BLACK) {
                winnerNick = blackNick;
            } else if (winner == Color.WHITE) {
                winnerNick = whiteNick;
            } else {
                winnerNick = "remis";
            }

            databaseHandler.saveGame(blackNick, whiteNick, winnerNick, board.getSize(), recorder.getMoves());
        } catch (SQLException e) {
//            throw new RuntimeException(e);
            broadcast.error("Problem z nawiązaniem kontaktu z bazą danych, nie udało się zapisać gry");
        }
    }
}
