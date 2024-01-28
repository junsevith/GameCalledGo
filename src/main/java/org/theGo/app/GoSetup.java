package org.theGo.app;

import org.theGo.game.Color;
import org.theGo.communication.Communicator;
import org.theGo.players.ComputerPlayer;
import org.theGo.players.GoPlayer;
import org.theGo.players.HumanPlayer;

import java.util.Arrays;
import java.util.Map;

/**
 * Aplikacja przygotowująca grę Go
 */
public class GoSetup extends AppMode {
    private GoPlayer player1;
    private GoPlayer player2;

    private int size = 0;

    public GoSetup(Communicator communicator) {
        super(communicator);
    }

    @Override
    public void start() {
        if (comm.confirm("Czy chcesz zagrać w grę przez internet?", false)) {
            webGame();
        } else {
            setPlayers();
            setSize();
            startGame();
        }

    }

    /**
     * Sets players depending on user input.
     */
    private void setPlayers() {
        Map<String, Color> colorMap = Map.of(
                "b", Color.BLACK,
                "c", Color.WHITE,
                "biały", Color.WHITE,
                "czarny", Color.BLACK,
                "black", Color.BLACK,
                "white", Color.WHITE);
        if (comm.confirm("Czy chcesz grać z komputerem?", false)) {
            Color color = comm.choose("Wybierz kolor:", colorMap, Arrays.asList("biały", "czarny"), null);
            player1 = new HumanPlayer(color, comm);
            player2 = new ComputerPlayer(color.opposite());
        } else {
            player1 = new HumanPlayer(Color.BLACK, comm);
            player2 = new HumanPlayer(Color.WHITE, comm);
        }
    }

    /**
     * Sets size of board depending on user input.
     */
    private void setSize() {
        while (true) {
            size = comm.set("Podaj wielkość planszy: ", Integer::parseInt, 9);
            if (5 <= size && size <= 19) {
                break;
            }
            comm.error("Rozmiar planszy musi być z przedziału [5, 19]");
        }
        comm.accept("Plansza ustawiona");
    }

    /**
     * Starts an internet game.
     */
    private void webGame() {
    }

    /**
     * Starts a local game.
     */
    private void startGame() {
        new GoGame(player1, player2, comm, size).start();
    }
}
