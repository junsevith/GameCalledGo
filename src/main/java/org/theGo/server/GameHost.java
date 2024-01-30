package org.theGo.server;

import org.theGo.app.GoGame;
import org.theGo.app.GoInit;
import org.theGo.communication.Broadcast;
import org.theGo.communication.Communicator;
import org.theGo.game.Color;
import org.theGo.players.HumanPlayer;

public class GameHost {
    private final String roomName;
    
    private final String nickname;
    private final Color color;

    private final Communicator comm;
    private final int size;

    public GameHost(String roomName, String nickname, Color color, Communicator comm, int size) {
        this.roomName = roomName;
        this.nickname = nickname;
        this.color = color;
        this.comm = comm;
        this.size = size;
    }

    public String getRoomName() {
        return roomName;
    }

    public void join(String name, Communicator comm2) {
        HumanPlayer player1 = new HumanPlayer(color, comm, nickname);
        HumanPlayer player2 = new HumanPlayer(color.opposite(), comm2, name);
        Communicator broadcast = new Broadcast(comm, comm2);
        new GoGame(player1, player2, broadcast, size).start();
        new Thread(new GoInit(comm)).start();
        new GoInit(comm2).start();
    }

    public String toString() {
        return roomName + " " + nickname + " " + color + " " + size;
    }
}
