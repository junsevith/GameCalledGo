package org.theGo.players;

import org.theGo.game.Color;
import org.theGo.game.GoBoard;
import org.theGo.game.Move;

public class ComputerPlayer extends GoPlayer {
   public ComputerPlayer(Color color) {
      super(color);
   }


   @Override
   public Move takeTurn(GoBoard board) {
      return new Move(super.color, Move.Type.PASS);
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
