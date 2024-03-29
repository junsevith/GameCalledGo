package org.theGo.app;

import org.theGo.communication.Communicator;
import org.theGo.database.GoLoad;
import org.theGo.server.GameLobby;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

/**
 * Aplikacja do wyboru trybu gry
 */
public class GoInit extends AppMode implements Runnable {
    private final Map<String, Function<Communicator, AppMode>> modes = Map.of(
            "graj", this::gameApp,
            "g", this::gameApp,
            "game", this::gameApp,
            "gra", this::gameApp,

            "wczytaj", this::loadApp,
            "browse", this::loadApp,
            "load", this::loadApp,
            "l", this::loadApp,
            "w", this::loadApp
    );

    public GoInit(Communicator communicator) {
        super(communicator);
    }

    @Override
    public void start() {
        System.out.println("Connected: " + comm);
        try {
            do {
                comm.choose("Chcesz grać czy wczytać grę?", modes, Arrays.asList("Graj", "Wczytaj"), 0, true ).apply(comm).start();

            } while (!GameLobby.getInstance().isWaiting(comm));
        } catch (RuntimeException e) {
            System.out.println("Connection closed: " + comm);
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
    }

    private AppMode gameApp(Communicator communicator) {
        return new GoSetup(communicator);
    }

    private AppMode loadApp(Communicator communicator) {
        AppMode mode;
        try {
            mode = new GoLoad(communicator);
        } catch (Exception e) {
            comm.error("Problem z dostępem do bazy danych, wracam do wyboru trybu gry");
            mode = this;
        }
        return mode;
    }

    @Override
    public void run() {
        start();
    }
}
