package org.theGo.app;

import org.theGo.game.Color;
import org.theGo.communication.Communicator;
import org.theGo.players.ComputerPlayer;
import org.theGo.players.GoPlayer;
import org.theGo.players.HumanPlayer;
import org.theGo.server.GameHost;
import org.theGo.server.GameLobby;
import org.theGo.server.LobbyBrowser;

import java.util.Arrays;
import java.util.Map;

/**
 * Aplikacja przygotowująca grę Go
 */
public class GoSetup extends AppMode {
    private GoPlayer player1;
    private GoPlayer player2;

    private String nickname;

    private int size = 0;
    private Color color;

    public GoSetup(Communicator communicator) {
        super(communicator);
    }

    @Override
    public void start() {
        if (comm.confirm("Czy chcesz zagrać w grę przez internet?", false)) {
            webGame();
        } else {
            localGame();
        }

    }

    /**
     * Sets players depending on user input.
     */
    private void setPlayers() {
        if (comm.confirm("Czy chcesz grać z komputerem?", false)) {
            setColor();
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
    }

    private void setColor() {
        Map<String, Color> colorMap = Map.of(
                "b", Color.BLACK,
                "c", Color.WHITE,
                "biały", Color.WHITE,
                "czarny", Color.BLACK,
                "black", Color.BLACK,
                "white", Color.WHITE);
        color = comm.choose("Wybierz kolor:", colorMap, Arrays.asList("biały", "czarny"), null);
    }

    /**
     * Starts an internet game.
     */
    private void webGame() {
        if (nickname == null) {
            nickname = comm.ask("Podaj swój nick: ");
        }
        if (comm.confirm("Czy chcesz założyć nowy pokój?", false)) {
            String roomName = comm.ask("Podaj nazwę pokoju: ");
            setSize();
            setColor();
            comm.message("Oczekiwanie na gracza...");
            GameLobby.getInstance().addPlayer(new GameHost(roomName, nickname, color, comm, size));
        } else {
            GameHost host = new LobbyBrowser(comm).start();
            host.join(nickname, comm);
        }
    }


    /**
     * Starts a local game.
     */
    private void localGame() {
        setPlayers();
        setSize();
        new GoGame(player1, player2, comm, size).start();
        start();
    }
}
