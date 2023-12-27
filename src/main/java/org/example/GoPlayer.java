package org.example;

import java.io.*;

public abstract class GoPlayer {
   final Color color;
   GoPlayer(Color color){
      this.color = color;
   }
   abstract String takeTurn(GoBoard board) throws IOException;

   abstract String getName();

   abstract boolean askFinish();
}
