package org.theGo.communication;

import org.theGo.game.Color;
import org.theGo.game.GoBoard;

import java.io.*;
import java.util.*;
import java.util.function.Function;

public class TermComm extends Communicator {

    private final BufferedReader in;
    private final PrintWriter out;

    private final String answer_wrong = "The answer could not be understood.\n";

    public TermComm(InputStream in, OutputStream out) {
        this.in = new BufferedReader(new InputStreamReader(in));
        this.out = new PrintWriter(out, true);
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
    public void deny(String message) {
        message(message);
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

    private final Set<String> confirmations = Set.of("y", "yes", "tak", "t", "ok", "accept");
    private final Set<String> denials = Set.of("n", "no", "nie", "deny");
    private final Set<String> defaultSet = Set.of("default", "domyślna", "d", "domyslna", "");

    @Override
    public boolean confirm(String question, Boolean defaultChoice) {
        String answer;
        if (defaultChoice != null) {
            answer = ask(question + " (default: " + (defaultChoice ? "yes" : "no") + ")");
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
            return confirm(answer_wrong + question, defaultChoice);
        }
    }


    @Override
    public <T> T set(String question, Function<String, T> parser, T defaultValue) {
        String answer;
        if (defaultValue != null) {
            answer = ask(question + " (default: " + defaultValue + ")");
            if (defaultSet.contains(answer.toLowerCase())) {
                return defaultValue;
            }
        } else {
            answer = ask(question);
        }

        try {
            return parser.apply(answer);
        } catch (Exception e) {
            return set(answer_wrong + question, parser, defaultValue);
        }
    }

    @Override
    public <T> T choose(String question, Map<String, T> map,  List<String> options, Integer defaultChoice) {
        String answer;
        if (defaultChoice != null) {
            answer = ask(question + " (default: " + options.get(defaultChoice) + ", type o to see options)");
            if (defaultSet.contains(answer.toLowerCase())) {
                return map.get(options.get(defaultChoice));
            }
        } else {
            answer = ask(question + " (type o to see options)");
        }

        if (answer.equals("o")) {
            out.println("Options:");
            message(options.toString());
            return choose( question, map, options, defaultChoice);
        }

        T parsed = map.get(answer);
        if (parsed == null) {
            return choose(answer_wrong + question, map, options, defaultChoice);
        } else {
            return parsed;
        }
    }
}
