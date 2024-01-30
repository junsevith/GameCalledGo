package org.theGo;

import org.theGo.app.GoInit;
import org.theGo.communication.TermComm;
import org.theGo.server.GoServer;

public class Main {
//    public static void main(String[] args) {
//        new GoInit(new TermComm(System.in, System.out, System.err)).start();
//    }

    public static void main(String[] args) {
        new GoServer().start(6666);
    }
}
