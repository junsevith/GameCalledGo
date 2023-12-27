package org.example;

import java.io.*;
import java.util.Set;

public class HumanPlayer extends GoPlayer {
   BufferedReader in;

   PrintWriter out;

   public HumanPlayer(Color color, BufferedReader in, PrintWriter out) {
      super(color);
      this.in = in;
      this.out = out;
   }

   @Override
   public String takeTurn(GoBoard board) {
      String output = "";
      while (output.isEmpty()){
         try {
            String line = in.readLine();
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
            out.println("Niepoprawne współrzędne");
         } catch (IllegalArgumentException e) {
            out.println("Nie można ustawić kamienia na tym polu");
         } catch (IOException e) {
            throw new RuntimeException(e);
         }
      }
      return output;
   }

   @Override
   String getName() {
      if (super.color == Color.BLACK) {
         return "grający czarnymi";
      } else {
         return "grający białymi";
      }
   }

   @Override
   boolean askFinish() {
      out.println("Gracz: " + color + ", czy chcesz zakończyć grę? (t/n)");
      return readAnswer(Set.of("t", "tak"), Set.of("n", "nie"));
   }

   public  boolean readAnswer(Set<String> goodAnswers, Set<String> badAnswers) {
      try {
         String line = in.readLine();
         if (line.isEmpty()) {
            out.println("Zastosowano domyślną odpowiedź: " + badAnswers.iterator().next());
            return false;
         } else if (goodAnswers.contains(line)) {
            return true;
         } else if (badAnswers.contains(line)) {
            return false;
         } else {
            throw new IllegalArgumentException();
         }
      } catch (IOException e) {
         throw new RuntimeException(e);
      } catch (IllegalArgumentException e) {
         out.println("Niepoprawna odpowiedź");
         return readAnswer(goodAnswers, badAnswers);
      }
   }
}


