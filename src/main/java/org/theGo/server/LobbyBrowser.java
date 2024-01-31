package org.theGo.server;

import org.theGo.communication.Communicator;

public class LobbyBrowser {

    private final GameLobby lobby = GameLobby.getInstance();

    private boolean Continue = true;

    private final Communicator comm;

    public LobbyBrowser(Communicator comm) {
        this.comm = comm;
    }


    public GameHost start() {
        GameHost host = null;
        while (Continue) {
            comm.message("Dostępne pokoje: ");
            String hosts = lobby.getHosts();
            if (hosts.isEmpty()) {
                comm.message("Brak dostępnych pokoi");
                return null;
            }
            comm.display(hosts);
            String name = comm.ask("Podaj nazwę pokoju, do którego chcesz dołączyć: ", false);
            host = lobby.connectToHost(name);
            if (host == null) {
                comm.error("Nie ma takiego pokoju");
            } else {
                Continue = false;
            }
        }
        return host;
    }
}
