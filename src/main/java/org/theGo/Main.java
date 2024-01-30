package org.theGo;

import org.theGo.app.GoInit;
import org.theGo.communication.*;

import java.net.ServerSocket;

public class Main {
//    public static void main(String[] args) {
//        new GoInit(new TermComm(System.in, System.out, System.err));
//    }

    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)){
            while (true) {
                new Thread(new GoInit(new ServComm(serverSocket.accept()))).start();
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    public static void main(String[] args) {
        Main main = new Main();
        main.start(6666);
    }
}
