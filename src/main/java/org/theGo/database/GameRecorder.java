package org.theGo.database;

import org.theGo.game.Color;
import org.theGo.game.Move;

import java.util.ArrayList;

public class GameRecorder {
   private final ArrayList<Move> moves = new ArrayList<>();

   private final ArrayList<Color[][]> boardStates = new ArrayList<>();

   public void recordBoardState(Color[][] boardState) {
      boardStates.add(boardState);
   }

   public void recordMove(Move move) {
      moves.add(move);
   }

   public boolean gameHalted() {
      if (moves.size() < 2) {
         return false;
      } else {
         Move lastMove = moves.getLast();
         Move secondLastMove = moves.get(moves.size() - 2);
         return lastMove.isType(Move.Type.PASS) && secondLastMove.isType(Move.Type.PASS);
      }
   }

   public ArrayList<Move> getMoves() {
      return moves;
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
