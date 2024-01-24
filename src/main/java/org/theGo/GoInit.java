package org.theGo;

import org.theGo.communication.Communicator;
import org.theGo.players.ComputerPlayer;
import org.theGo.players.GoPlayer;
import org.theGo.players.HumanPlayer;

import java.util.Map;

public class GoInit {
    Communicator comm;
    GoPlayer player1;
    GoPlayer player2;

    int size = 0;

    public GoInit(Communicator communicator) {
        comm = communicator;
        setPlayers();
    }

    /**
     * Ustawia graczy oraz komputery
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
            Color color = comm.choose("Wybierz kolor:", colorMap, null);
            player1 = new HumanPlayer(color, comm);
            player2 = new ComputerPlayer(color.opposite());
            setSize();
            startGame();
        } else {
            if (comm.confirm("Czy chcesz zagrać w grę przez internet?", false)) {
                webGame();
            } else {
                player1 = new HumanPlayer(Color.BLACK, comm);
                player2 = new HumanPlayer(Color.WHITE, comm);
                setSize();
                startGame();
            }
        }
    }

    private void setSize() {
        while (true) {
            size = comm.set("Podaj wielkość planszy: ", Integer::parseInt, 9);
            if (5 <= size && size <= 19) {
                break;
            }
            comm.deny("Rozmiar planszy musi być z przedziału [5, 19]");
        }
        comm.accept("Plansza ustawiona");
    }

    private void webGame() {
    }

    private void startGame() {
        new GoGame(player1, player2, comm, size).startGame();
    }
}
