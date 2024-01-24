package org.theGo;

import org.theGo.communication.TermComm;

public class Main {
   public static void main(String[] args) {
      GoInit goInit = new GoInit(new TermComm(System.in, System.out));
   }
}
