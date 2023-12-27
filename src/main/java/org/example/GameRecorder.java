package org.example;

import java.util.ArrayList;

public class GameRecorder {
   private final ArrayList<String> moves = new ArrayList<>();

   private final ArrayList<Color[][]> boardStates = new ArrayList<>();

   public void recordBoardState(Color[][] boardState) {
      boardStates.add(boardState);
   }

   public void recordMove(String move) {
      moves.add(move);
   }

   public boolean gameHalted() {
      if (moves.size() < 2) {
         return false;
      } else {
         String lastMove = moves.getLast().split(" ")[1].strip();
         String secondLastMove = moves.get(moves.size() - 2).split(" ")[1].strip();
         return lastMove.equals("pas") && secondLastMove.equals("pas");
      }
   }

   public String getMoveHistory() {
      StringBuilder sb = new StringBuilder();
      for (String move : moves) {
         sb.append(move);
         sb.append("\n");
      }
      return sb.toString();
   }

   public Color[][] getState(int i){
      if (i > boardStates.size() - 1 || i < -boardStates.size() ) {
         return new Color[0][0];
      }
      if (i >= 0) {
         return boardStates.get(i);
      } else {
         return boardStates.get(boardStates.size() + i);
      }
   }
}
