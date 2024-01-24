package org.theGo.players;

import org.theGo.Color;
import org.theGo.GoBoard;

public class ComputerPlayer extends GoPlayer {
   public ComputerPlayer(Color color) {
      super(color);
   }


   @Override
   public String takeTurn(GoBoard board) {
      return color + " pas computer";
   }

   @Override
   public String getName() {
      if (super.color == Color.BLACK) {
         return "komputer grający czarnymi";
      } else {
         return "komputer grający białymi";
      }
   }

   @Override
   public boolean askFinish() {
      return true;
   }

   @Override
   public void message(String message) {

   }
}
