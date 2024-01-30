package org.theGo.communication;

import org.theGo.game.Color;
import org.theGo.game.GoBoard;
import org.theGo.game.Move;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;

public class ServComm extends TermComm {

    Socket socket;
    public ServComm(Socket socket) throws IOException {
        super(socket.getInputStream(), socket.getOutputStream(), socket.getOutputStream());
        this.socket = socket;
    }

    @Override
    public String askRead(String question) {
        return super.askRead(question);
    }

    @Override
    public void write(String message) {
        super.write(message.replace('\n', '|'));
    }

    @Override
    public String ask(String question) {
        return super.askRead("ASK_" + question);
    }

    @Override
    public boolean confirm(String question, Boolean defaultChoice) {
        return super.confirm("CNF_" + question, defaultChoice);
    }

    @Override
    public <T> T choose(String question, Map<String, T> map, java.util.List<String> options, Integer defaultChoice) {
        StringBuilder sb = new StringBuilder();
        sb.append("CHS_").append(question).append("%");
        for (String s : options) {
            sb.append(s).append("%");
        }
        return super.choose(sb.toString(), map, options, defaultChoice);
    }

    @Override
    public <T> T set(String question, java.util.function.Function<String, T> parser, T defaultChoice) {
        return super.set("SET_" + question, parser, defaultChoice);
    }

    @Override
    public Move takeMove(String question, Color color) {
        return super.takeMove("MOV_" + question, color);
    }

    @Override
    public void message(String message) {
        write("MSG_" + message);
    }


    @Override
    public void error(String message) {
        super.error("ERR_" + message);
    }

    @Override
    public void displayBoard(GoBoard goBoard) {
        Color[][] board = goBoard.getState();
        int boardSize = goBoard.getSize();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == null) {
                    sb.append("N");
                } else if (board[i][j] == Color.BLACK) {
                    sb.append("B");
                } else {
                    sb.append("W");
                }
                sb.append(" ");
            }
            sb.append("|");
        }
        sb.deleteCharAt(sb.length() - 1);
        out.println("BRD_" + sb);
    }

    @Override
    public void displayScore(int blackPoints, int whitePoints) {
        write("SCR_" + blackPoints + " " + whitePoints);
    }

    @Override
    public void display(String message) {
        write("DSP_" + message);
    }

    @Override
    public void close() {
        super.close();
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException("Cannot close socket",e);
        }
    }


}
