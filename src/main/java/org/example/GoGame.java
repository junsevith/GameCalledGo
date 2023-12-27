package org.example;

import java.io.*;
import java.util.Arrays;
import java.util.Set;

public class GoGame {

   GoBoard board;
   GoPlayer black;
   GoPlayer white;

   BufferedReader in;
   PrintWriter out;

   GameRecorder recorder = new GameRecorder();

   public GoGame(InputStream in, OutputStream out) {
      this.in = new BufferedReader(new InputStreamReader(in));
      this.out = new PrintWriter(out, true);
   }

   /**
    * Czyta odpowiedź z wejścia i sprawdza, czy jest w jednym ze zbiorów.
    * Jeśli nie jest w żadnym z nich, wypisuje komunikat o niepoprawnej odpowiedzi i pyta ponownie.
    *
    * @param goodAnswers  zbiór pozytywnych odpowiedzi
    * @param badAnswers  zbiór negatywnych odpowiedzi
    * @return Jeśli jest w zbiorze goodAnswers, zwraca true, jeśli w badAnswers, zwraca false.
    */
   public boolean readAnswer(Set<String> goodAnswers, Set<String> badAnswers) {
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

   /**
    * Rozpoczyna grę
    */
   public void startGame() throws IOException {
      out.println("Witaj w grze Go!");
      setBoard();
      setPlayers();
      beginGame();
      countPoints();
   }

   /**
    * Ustawia rozmiar planszy
    */
   private void setBoard() throws IOException {
      out.println("Podaj rozmiar planszy: ");
      try {
         String line = in.readLine();
         int size;
         if (line.isEmpty()) {
            size = 9;
            out.println("Ustawiam domyślny rozmiar planszy: " + size + "x" + size);
         } else {
            size = Integer.parseInt(line);
         }
         if (size < 5 || size > 19) {
            throw new IllegalArgumentException();
         }
         if (size % 2 == 0) {
            out.println("Zaleca się nieparzysty rozmiar planszy");
         }
         board = new GoBoard(size);
         out.println("Plansza ustawiona");
      } catch (NumberFormatException e) {
         out.println("Niepoprawny rozmiar planszy");
         setBoard();
      } catch (IllegalArgumentException e) {
         out.println("Rozmiar planszy musi być z przedziału [5, 19]");
         setBoard();
      }
   }

   /**
    * Ustawia graczy oraz komputery
    */
   private void setPlayers() {
      out.println("Czy chcesz grać z komputerem? (t/n)");
      if (readAnswer(Set.of("t", "tak"), Set.of("n", "nie"))) {
         out.println("Wybierz kolor: (b/c)");
         if (readAnswer(Set.of("b", "biały"), Set.of("c", "czarny"))) {
            black = new HumanPlayer(Color.BLACK, in, out);
            white = new ComputerPlayer(Color.WHITE);
         } else {
            black = new ComputerPlayer(Color.BLACK);
            white = new HumanPlayer(Color.WHITE, in, out);
         }
      } else {
         black = new HumanPlayer(Color.BLACK, in, out);
         white = new HumanPlayer(Color.WHITE, in, out);
      }
   }

   /**
    * Rozpoczyna grę
    */
   private void beginGame() throws IOException {
      out.println("Zaczynamy grę!");
      GoPlayer activePlayer = black;
//      recorder.recordBoardState(board.getState());
      while (gameIsNotOver()) {
         out.println(board.printBoard());
         out.println("Punkty czarnego: " + board.getCounter().getWhiteKilled());
         out.println("Punkty białego: " + board.getCounter().getBlackKilled());
         out.println("Teraz ruch wykonuje " + activePlayer.getName());

         while (true) {
            String move = activePlayer.takeTurn(board);

            if (!move.split(" ")[1].equals("pas") && Arrays.deepEquals(board.getState(), recorder.getState(-2))) {
               out.println("Nie można wykonać ruchu ko");
               board = GoBoard.generateBoard(board.getSize(), recorder.getMoveHistory());
            } else {
               recorder.recordMove(move);
               recorder.recordBoardState(board.getState());
               break;
            }
         }

         if (activePlayer == black) {
            activePlayer = white;
         } else {
            activePlayer = black;
         }
      }
   }

   /**
    * Sprawdza, czy gra się nie skończyła
    * @return true, jeśli gra się nie skończyła, false, jeśli tak
    */
   private boolean gameIsNotOver() {
      if (recorder.gameHalted()) {
         return !(black.askFinish() && white.askFinish());
      } else {
         return true;
      }
   }

   private void countPoints() {
      int blackPoints = board.getCounter().getWhiteKilled() + board.countTerritory(Color.BLACK);
      int whitePoints = board.getCounter().getBlackKilled() + board.countTerritory(Color.WHITE);
      out.println("Punkty czarnego: " + blackPoints);
      out.println("Punkty białego: " + whitePoints);
      if (blackPoints > whitePoints) {
         out.println("Wygrał czarny!");
      } else if (whitePoints > blackPoints) {
         out.println("Wygrał biały!");
      } else {
         out.println("Remis!");
      }
   }
}
