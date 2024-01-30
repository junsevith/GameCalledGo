package org.theGo.server;

import org.theGo.app.GoInit;
import org.theGo.communication.ServComm;

import java.net.ServerSocket;

public class GoServer {
    public void start(int port) {
        System.out.println("Server started");
        try (ServerSocket serverSocket = new ServerSocket(port)){
            while (true) {
                new Thread(new GoInit(new ServComm(serverSocket.accept()))).start();
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
}
