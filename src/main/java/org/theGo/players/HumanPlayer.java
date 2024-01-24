package org.theGo.players;

import org.theGo.Color;
import org.theGo.GoBoard;
import org.theGo.communication.Communicator;

public class HumanPlayer extends GoPlayer {
   Communicator comm;

   public HumanPlayer(Color color, Communicator communicator) {
      super(color);
      comm = communicator;
   }

   @Override
   public String takeTurn(GoBoard board) {
      String output = "";
      while (output.isEmpty()){
         try {
            String line = comm.ask("Podaj współrzędne ruchu:");
            String[] args = line.split(" ");

            if (line.strip().equals("pas")){
               output = color + " pas";
               continue;
            }

            int x = Integer.parseInt(args[0]);
            int y = Integer.parseInt(args[1]);

            if (x < 1 || x > board.getSize() || y < 1 || y > board.getSize()){
               throw new IllegalArgumentException();
            }

            if (!board.placeStone(x - 1, y - 1, super.color)){
               throw new IllegalArgumentException();
            } else {
               output = color + " " + x + " " + y;
            }

         } catch (NumberFormatException e) {
            comm.deny("Niepoprawne współrzędne");
         } catch (IllegalArgumentException e) {
            comm.deny("Nie można ustawić kamienia na tym polu");
         }
      }
      return output;
   }

   @Override
   public String getName() {
      if (super.color == Color.BLACK) {
         return "grający czarnymi";
      } else {
         return "grający białymi";
      }
   }

   @Override
   public boolean askFinish() {
      return comm.confirm("Czy chcesz zakończyć grę?", false);
   }

   @Override
   public void message(String message) {
        comm.message(message);
   }
}


