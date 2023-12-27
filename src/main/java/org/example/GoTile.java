package org.example;

import java.util.*;

public class GoTile {
   /**
    * Kolor kamienia na polu, jeśli null to pole jest puste
    */
   private Color stoneColor = null;

   /**
    * Tablica sąsiadów pola
    * 0 - Góra
    * 1 - Dół
    * 2 - Lewo
    * 3 - Prawo
    */
   private GoTile[] neighbors = new GoTile[4];

   private final Set<GoTile> breathTiles = new HashSet<>();

   /**
    * Licznik zbitych kamieni
    */
   private final GameCounter counter;

   public GoTile(GameCounter counter) {
      this.counter = counter;
   }

   public void setNeighbors(GoTile[] neighbors) {
      this.neighbors = neighbors;
   }

   /**
    * Resetuje pole do stanu początkowego
    */
   private void resetTile() {
      stoneColor = null;
      breathTiles.clear();
   }

   /**
    * Zwraca kolor kamienia na polu
    * Null, jeśli pole jest puste
    *
    * @return Kolor kamienia na polu
    */
   public Color getStoneColor() {
      return stoneColor;
   }

   /**
    * Zwraca sąsiadów o danym kolorze
    *
    * @param color Kolor do dopasowania
    * @return Lista sąsiadów o danym kolorze
    */
   public List<Integer> getNeighbors(Color color) {
      List<Integer> matchingNeighbors = new ArrayList<>();
      for (int i = 0; i < 4; i++) {
         if (neighbors[i] != null && neighbors[i].getStoneColor() == color) {
            matchingNeighbors.add(i);
         }
      }
      return matchingNeighbors;
   }

   /**
    * Dodaje oddechy do kamienia
    * @param tiles oddechy do dodania
    */
   public void inheritBreath(Set<GoTile> tiles) {
      breathTiles.addAll(tiles);
   }

   /**
    * Dodaje oddech do kamienia
    * @param tile oddech do dodania
    */
   public void inheritBreath(GoTile tile) {
      breathTiles.add(tile);
   }

   /**
    * Odbiera oddech z danego kierunku
    * @param breath odbiera oddech z tego pola
    */
   public void looseBreath(GoTile breath, Set<GoTile> already) {
      already.add(this);
      breathTiles.remove(breath);

//      Jeśli używamy checkNeighborsV2() to musi być ten warunek
//      if (stoneColor == null) {
//         return;
//      }

      for (int i = 0; i < 4; i++) {
         GoTile neighbor = neighbors[i];
         if ( neighbor != null && neighbor.stoneColor == stoneColor) {
            if (neighbor != breath && !already.contains(neighbor)) {
               neighbor.looseBreath(breath, already);
            }
         }
      }

      if (breathTiles.isEmpty()) {
         killStone();
      }
   }

   /**
    * Zabija kamień na polu
    */
   private void killStone() {
      counter.addCapturedStone(stoneColor);

      //oddaje oddech sąsiadom przeciwnego koloru
      for (Integer dir : getNeighbors(stoneColor.opposite())) {
         neighbors[dir].inheritBreath(this);
      }

      resetTile();
   }

   /**
    * Ustawia kamień na polu
    *
    * @param color Kolor kamienia
    * @return True, jeśli udało się ustawić kamień
    */
   public boolean placeStone(Color color) {
      if (stoneColor != null) {
         return false;
      }  else if (getNeighbors(null).isEmpty()) {
         //Jeśli miejsce otoczone jest kamieniami

         for (Integer direction : getNeighbors(color)) {
            if (!neighbors[direction].onlyBreath(this)) {
               //sprawdza, czy można odziedziczyć oddechy od sąsiadów tego samego koloru
               setupStone(color);
               return true;
            }
         }

         for (Integer dir : getNeighbors(color.opposite())) {
            if (neighbors[dir].onlyBreath(this)) {
               //sprawdza, czy można zabić kamienie przeciwnika
               setupStone(color);
               return true;
            }
         }

         //Jeśli nie można odziedziczyć oddechów ani zabić kamieni przeciwnika
         //ruch spowodowałby samobójstwo, więc nie można go wykonać
         return false;
      } else {
         setupStone(color);
         return true;
      }
   }

   /**
    * Ustawia na polu kamień danego koloru
    *
    * @param color Kolor kamienia
    */
   private void setupStone(Color color) {
      stoneColor = color;
      checkNeighborsV3();
   }

   /**
    * Sprawdza sąsiadów i aktualizuje oddechy oraz zależności
    */
   private void checkNeighborsV2() {
      //zabiera oddechy wszystkim sąsiadom przeciwnego koloru
      for (Integer direction : getNeighbors(stoneColor.opposite())) {
         neighbors[direction].looseBreath(this, Set.of(this));
      }

      //ustawia sobie oddechy od pustych sąsiadów
      for (Integer direction : getNeighbors(null)) {
         this.inheritBreath(neighbors[direction]);
      }

      //zabiera oddechy od sąsiadów tego samego koloru
      List<Integer> sameColorNeighbors = getNeighbors(stoneColor);
      if (!sameColorNeighbors.isEmpty()) {

         for (Integer direction : sameColorNeighbors) {
            this.inheritBreath(neighbors[direction].breathTiles);
         }

         breathTiles.remove(this);

         for (Integer direction : sameColorNeighbors) {
            neighbors[direction].inheritBreath(this.breathTiles);
            neighbors[direction].looseBreath(this, Set.of(this));
         }
      }
   }


   /**
    * Sprawdza sąsiadów i aktualizuje oddechy oraz zależności
    */
   private void checkNeighborsV3() {
      for (int i = 0; i < 4; i++) {
         GoTile neighbor = neighbors[i];
         if (neighbor != null ) {
            if (neighbor.stoneColor == stoneColor.opposite()) {
               //zabiera oddechy od sąsiadów tego samego koloru
               neighbor.looseBreath(this, new HashSet<>(Set.of(this)));
            } else if (neighbor.stoneColor == null) {
               //ustawia sobie oddechy od pustych sąsiadów
               this.inheritBreath(neighbor);
            }
         }
      }


      if (!getNeighbors(stoneColor).isEmpty()) {
         //zabiera oddechy od sąsiadów tego samego koloru
         for (int i = 0; i < 4; i++) {
            GoTile neighbor = neighbors[i];
            if (neighbor != null) {
               if (neighbor.stoneColor == stoneColor) {
                  this.inheritBreath(neighbor.breathTiles);
               }
            }
         }

         // usuwa siebie z oddechów, ponieważ dziedziczenie oddechów od sąsiadów nas tu dodało
//         this.looseBreath(this, this, this);
         breathTiles.remove(this);
         //dodaje oddechy do sąsiadów tego samego koloru
         for (int i = 0; i < 4; i++) {
            GoTile neighbor = neighbors[i];
            if (neighbor != null) {
               if (neighbor.stoneColor == stoneColor) {
                  neighbor.inheritBreath(this.breathTiles);
                  neighbor.looseBreath(this, new HashSet<>(Set.of(this)));
               }
            }
         }
      }
   }

   /**
    * Sprawdza, czy podane pole jest jedynym oddechem tego pola
    * @param tile pole do sprawdzenia
    * @return true, jeśli podane pole jest jedynym oddechem tego pola
    */
   private boolean onlyBreath(GoTile tile) {
      return breathTiles.contains(tile) && breathTiles.size() == 1;
   }

   public boolean countTerritory(Color color, Set<GoTile> territory) {
      territory.add(this);
      boolean isTerritory = true;
      for (int i = 0; i < 4; i++) {
         if (neighbors[i] != null) {
            if (neighbors[i].getStoneColor() == null && !territory.contains(neighbors[i])){
               isTerritory = isTerritory && neighbors[i].countTerritory(color, territory);
            } else if (neighbors[i].getStoneColor() == color.opposite()) {
               return false;
            }
         }
      }
      return isTerritory;
   }
}
