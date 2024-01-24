package org.theGo;

import java.util.HashSet;
import java.util.Set;

public class GoBoard {
   private final GoTile[][] board = new GoTile[19][19];

   private final int boardSize;

   private final GameCounter counter = new GameCounter();

   /**
    * Tworzy planszę o podanym rozmiarze
    *
    * @param boardSize rozmiar planszy
    */
   public GoBoard(int boardSize) {
      this.boardSize = boardSize;
      setNeighbors();
   }

   public GameCounter getCounter() {
      return counter;
   }

   /**
    * Ustawia sąsiadów dla każdego pola
    */
   private void setNeighbors() {
      for (int i = 0; i < boardSize; i++) {
         for (int j = 0; j < boardSize; j++) {
            board[i][j] = new GoTile(counter);
         }
      }

      for (int i = 0; i < boardSize; i++) {
         for (int j = 0; j < boardSize; j++) {
            GoTile[] neighbors = new GoTile[4];
            if (i > 0) {
               neighbors[0] = board[i - 1][j];
            }
            if (i < 18) {
               neighbors[1] = board[i + 1][j];
            }
            if (j > 0) {
               neighbors[2] = board[i][j - 1];
            }
            if (j < 18) {
               neighbors[3] = board[i][j + 1];
            }
            board[i][j].setNeighbors(neighbors);
         }
      }
   }

   /**
    * Ustawia kamień na planszy
    *
    * @param x     współrzędna x
    * @param y     współrzędna y
    * @param color kolor kamienia
    * @return true, jeśli udało się ustawić kamień, false, jeśli nie
    */
   public boolean placeStone(int x, int y, Color color) {
      return board[x][y].placeStone(color);
   }

   /**
    * Zwraca planszę w postaci Stringa
    *
    * @return plansza w postaci Stringa
    */
   public String printBoard() {
      StringBuilder sb = new StringBuilder();
      sb.append("  ");
      for (int i = 0; i < boardSize; i++) {
         sb.append(String.format("%3d", i + 1));
      }
      sb.append("\n");
      for (int i = 0; i < boardSize; i++) {

         sb.append(String.format("%2d", i + 1));
         sb.append("  ");
         for (int j = 0; j < boardSize; j++) {
            if (board[i][j].getStoneColor() == null) {
               sb.append("+");
            } else if (board[i][j].getStoneColor() == Color.BLACK) {
               sb.append("B");
            } else {
               sb.append("W");
            }
            sb.append("  ");
         }
         sb.append("\n");
      }
      sb.deleteCharAt(sb.length() - 1);
      return sb.toString();
   }

   public Color[][] getState() {
      Color[][] state = new Color[boardSize][boardSize];
      for (int i = 0; i < boardSize; i++) {
         for (int j = 0; j < boardSize; j++) {
            state[i][j] = board[i][j].getStoneColor();
         }
      }
      return state;
   }

   public int getSize() {
      return boardSize;
   }

   public static GoBoard generateBoard(int size, String moveHistory) {
      GoBoard board = new GoBoard(size);
      String[] moves = moveHistory.split("\n");
      for (String move : moves) {
         if (move.isEmpty()) {
            continue;
         }
         String[] args = move.split(" ");
         if (args[1].equals("pas")) {
            continue;
         }
         int x = Integer.parseInt(args[1]);
         int y = Integer.parseInt(args[2]);
         board.placeStone(x - 1, y - 1, Color.valueOf(args[0]));
      }
      return board;
   }

   public int countTerritory(Color color) {
      Set<GoTile> already = new HashSet<>();
      Set<GoTile> territory = new HashSet<>();

      for (int i = 0; i < boardSize; i++) {
         for (int j = 0; j < boardSize; j++) {
            Set<GoTile> temp = new HashSet<>();
            GoTile tile = board[i][j];
            if ( tile.getStoneColor() == null && !already.contains(tile)) {
               if (board[i][j].countTerritory(color, temp)) {
                  territory.addAll(temp);
               }
               already.addAll(temp);
            }
         }
      }

      return territory.size();
   }
}
