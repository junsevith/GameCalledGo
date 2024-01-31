package org.theGo.server;

import org.theGo.communication.Communicator;

import java.util.HashMap;
import java.util.Map;

public class GameLobby {
    private static volatile GameLobby instance = null;

    private final Map<String,GameHost> lobby = new HashMap<>();

    private GameLobby() {
    }

    public static GameLobby getInstance() {
        if (instance == null) {
            synchronized (GameLobby.class) {
                if (instance == null) {
                    instance = new GameLobby();
                }
            }
        }
        return instance;
    }

    public synchronized void addPlayer(GameHost host) {
        lobby.put(host.getRoomName(), host);
    }

    public synchronized String getHosts() {
        StringBuilder sb = new StringBuilder();
        for (String s : lobby.keySet()) {
            sb.append(lobby.get(s)).append("\n");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public synchronized GameHost connectToHost(String name) {
        GameHost host = lobby.get(name);
        lobby.remove(name);
        return host;
    }

    public synchronized boolean isWaiting(Communicator comm) {
        for (GameHost host : lobby.values()) {
            if (host.checkComm(comm)) {
                return true;
            }
        }
        return false;
    }
}
