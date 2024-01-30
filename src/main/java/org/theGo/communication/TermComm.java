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

    protected final BufferedReader in;
    protected final PrintWriter out;
    protected final PrintWriter err;

    private final String answerNotRecognized = "Nie rozpoznano odpowiedzi";
    private final String optionString = "wpisz o żeby zobaczyć dostępne opcje";
    private final String defaultString = "domyślnie:";

    public TermComm(InputStream in, OutputStream out, OutputStream err) {
        this.in = new BufferedReader(new InputStreamReader(in));
        this.out = new PrintWriter(out, true);
        this.err = new PrintWriter(err, true);
    }

    public String askRead(String question) {
        write(question);
        String answer;
        try {
            answer = in.readLine();
        } catch (IOException e) {
            close();
            throw new RuntimeException("Error while reading input, closing connection", e);
        }
        if (answer.equals("exit")) {
            close();
            throw new RuntimeException("Client exited");
        }
//        System.out.println("ANSWER:" + answer);
        return answer;
    }

    public void write(String message) {
        out.println(message);
    }

    @Override
    public String ask(String question) {
        return askRead(question);
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
        while (true) {
            String answer;
            if (defaultChoice != null) {
                answer = askRead(question +" (y/n)" + " (" + defaultString + " " + (defaultChoice ? "yes" : "no") + ")");
                if (defaultSet.contains(answer.toLowerCase())) {
                    return defaultChoice;
                }
            } else {
                answer = askRead(question + " (y/n)");
            }
            if (confirmations.contains(answer.toLowerCase())) {
                return true;
            } else if (denials.contains(answer.toLowerCase())) {
                return false;
            } else {
                error(answerNotRecognized);
            }
        }
    }

    @Override
    public <T> T choose(String question, Map<String, T> map, List<String> options, Integer defaultChoice) {
        while (true) {
            String answer;
            if (defaultChoice != null) {
                answer = askRead(question + " (" + defaultString + " " + options.get(defaultChoice) + ", " + optionString + ")");
                if (defaultSet.contains(answer.toLowerCase())) {
                    return map.get(options.get(defaultChoice));
                }
            } else {
                answer = askRead(question + " (type o to see options)");
            }

            if (answer.equals("o")) {
                out.println("Options:");
                write(options.toString());
                continue;
            }

            T parsed = map.get(answer);
            if (parsed != null) {
                return parsed;
            } else {
                error("Nie rozpoznano odpowiedzi");
            }
        }
    }

    @Override
    public <T> T set(String question, Function<String, T> parser, T defaultValue) {
        while (true) {
            String answer;
            if (defaultValue != null) {
                answer = askRead(question + " (" + defaultString + " " + defaultValue + ")");
                if (defaultSet.contains(answer.toLowerCase())) {
                    return defaultValue;
                }
            } else {
                answer = askRead(question);
            }

            try {
                return parser.apply(answer);
            } catch (Exception e) {
                error(answerNotRecognized);
            }
        }
    }

    @Override
    public Move takeMove(String question, Color color) {
        while (true) {
            try {
                Move output;
                String line = askRead(question);
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
            } catch (NumberFormatException e) {
                error(answerNotRecognized);
            }
        }
    }

    @Override
    public void message(String message) {
        write(message);
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
        write(sb.toString());
    }

    @Override
    public void displayScore(int blackPoints, int whitePoints) {
        write("Punkty czarnego: " + blackPoints + "\nPunkty białego: " + whitePoints);
    }

    @Override
    public void close() {

    }

    @Override
    public void error(String message) {
        err.println(message);
    }

    @Override
    public void display(String message) {
        write(message);
    }


}
