package org.theGo.app;

import org.theGo.communication.Communicator;
import org.theGo.database.GoLoad;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public class GoInit extends AppMode {
    Map<String, Function<Communicator, AppMode>> modes = Map.of(
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
        comm.choose("Chcesz grać czy wczytać grę?", modes, Arrays.asList("graj", "wczytaj"), 0).apply(comm).start();
    }

    private AppMode gameApp(Communicator communicator) {
        return new GoSetup(communicator);
    }

    private AppMode loadApp(Communicator communicator) {
        AppMode mode;
        try {
            mode = new GoLoad(communicator);
        } catch (Exception e) {

            throw new RuntimeException(e);
        }
        return mode;
    }
}
