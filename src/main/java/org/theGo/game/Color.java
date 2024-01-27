package org.theGo.game;

public enum Color {
   BLACK, WHITE;

   public Color opposite(){
      if(this == Color.BLACK){
         return Color.WHITE;
      } else {
         return Color.BLACK;
      }
   }
}