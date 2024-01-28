package org.theGo;

import org.theGo.app.GoInit;
import org.theGo.communication.TermComm;

public class Main {
    public static void main(String[] args) {
        new GoInit(new TermComm(System.in, System.out, System.err));
    }
}
