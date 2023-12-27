package org.example;

public enum Color {
   BLACK, WHITE;

   Color opposite(){
      if(this == Color.BLACK){
         return Color.WHITE;
      } else {
         return Color.BLACK;
      }
   }
}