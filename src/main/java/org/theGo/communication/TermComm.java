package org.theGo.communication;

import org.theGo.game.Color;
import org.theGo.game.GoBoard;
import org.theGo.game.Move;


import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * Class that allows to communicate with the user through the console.
 */
public class TermComm extends Communicator {

    private final BufferedReader in;
    private final PrintWriter out;
    private final PrintStream err;

    private final String answerNotRecognized = "Nie rozpoznano odpowiedzi";
    private final String optionString = "wpisz o żeby zobaczyć dostępne opcje";
    private final String defaultString = "domyślnie:";

    public TermComm(InputStream in, OutputStream out, OutputStream err) {
        this.in = new BufferedReader(new InputStreamReader(in));
        this.out = new PrintWriter(out, true);
        this.err = new PrintStream(err, true);
    }

    @Override
    public String ask(String question) {
        message(question);
        try {
            return in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void message(String message) {
        out.println(message);
    }

    @Override
    public void accept(String message) {
        message(message);
    }

    @Override
    public void error(String message) {
        err.println(message);
    }

    @Override
    public void display(String message) {
        message(message);
    }

    @Override
    public void displayBoard(GoBoard goBoard) {
        Color[][] board = goBoard.getState();
        int boardSize = goBoard.getSize();
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
                if (board[i][j] == null) {
                    sb.append("+");
                } else if (board[i][j] == Color.BLACK) {
                    sb.append("B");
                } else {
                    sb.append("W");
                }
                sb.append("  ");
            }
            sb.append("\n");
        }
        sb.deleteCharAt(sb.length() - 1);
        message(sb.toString());
    }

    @Override
    public void displayScore(int blackPoints, int whitePoints) {
        message("Punkty czarnego: " + blackPoints + "\nPunkty białego: " + whitePoints);
    }

    /**
     * Set of strings that are recognized as confirmation.
     */
    private final Set<String> confirmations = Set.of("y", "yes", "tak", "t", "ok", "accept");
    /**
     * Set of strings that are recognized as denial.
     */
    private final Set<String> denials = Set.of("n", "no", "nie", "deny");
    /**
     * Set of strings that are recognized as default choice.
     */
    private final Set<String> defaultSet = Set.of("default", "domyślna", "d", "domyslna", "");

    @Override
    public boolean confirm(String question, Boolean defaultChoice) {
        String answer;
        if (defaultChoice != null) {
            answer = ask(question + " (" + defaultString + " " + (defaultChoice ? "yes" : "no") + ")");
            if (defaultSet.contains(answer.toLowerCase())) {
                return defaultChoice;
            }
        } else {
            answer = ask(question);
        }
        if (confirmations.contains(answer.toLowerCase())) {
            return true;
        } else if (denials.contains(answer.toLowerCase())) {
            return false;
        } else {
            error(answerNotRecognized);
            return confirm(question, defaultChoice);
        }
    }


    @Override
    public <T> T set(String question, Function<String, T> parser, T defaultValue) {
        String answer;
        if (defaultValue != null) {
            answer = ask(question + " (" + defaultString + " " + defaultValue + ")");
            if (defaultSet.contains(answer.toLowerCase())) {
                return defaultValue;
            }
        } else {
            answer = ask(question);
        }

        try {
            return parser.apply(answer);
        } catch (Exception e) {
            error(answerNotRecognized);
            return set(question, parser, defaultValue);
        }
    }

    @Override
    public Move takeMove(String question, Color color) {
        try {
            Move output;
            String line = ask("Podaj współrzędne ruchu:");
            String[] args = line.split(" ");

            if (line.strip().equals("pas")) {
                output = new Move(color, Move.Type.PASS);
            } else if (line.strip().equals("resign")) {
                output = new Move(color, Move.Type.RESIGN);
            } else {
                int x = Integer.parseInt(args[0]);
                int y = Integer.parseInt(args[1]);
                output = new Move(color, Move.Type.MOVE, x, y);
            }
            return output;
        } catch (Exception e) {
            error(answerNotRecognized);
            return takeMove(question, color);
        }
    }

    @Override
    public <T> T choose(String question, Map<String, T> map, List<String> options, Integer defaultChoice) {
        String answer;
        if (defaultChoice != null) {
            answer = ask(question + " (" + defaultString + " " + options.get(defaultChoice) + ", " + optionString + ")");
            if (defaultSet.contains(answer.toLowerCase())) {
                return map.get(options.get(defaultChoice));
            }
        } else {
            answer = ask(question + " (type o to see options)");
        }

        if (answer.equals("o")) {
            out.println("Options:");
            message(options.toString());
            return choose(question, map, options, defaultChoice);
        }

        T parsed = map.get(answer);
        if (parsed == null) {
            error("Nie rozpoznano odpowiedzi");
            return choose(question, map, options, defaultChoice);
        } else {
            return parsed;
        }
    }
}
