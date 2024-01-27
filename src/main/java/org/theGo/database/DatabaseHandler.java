package org.theGo.database;

import org.theGo.game.Color;
import org.theGo.game.Move;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseHandler {
    public String moveSeparator = "#";
    private final Database db;
    public DatabaseHandler(Database db) {
        this.db = db;
    }

    public void saveGame(String blackNick, String whiteNick, String winner, int boardSize, ArrayList<Move> moves) throws SQLException {
        String query = "INSERT INTO `games` (`id`, `black`, `white`, `winner`, `boardSize`, `moves`,`date`) VALUES (NULL, '" + blackNick + "', '" + whiteNick + "', '" + winner + "', '" + boardSize + "', '" + convertMovesToString(moves) + "', NOW());";
        db.insert(query);
    }

    public String convertMovesToString(ArrayList<Move> moves) {
        StringBuilder sb = new StringBuilder();
        for (Move move : moves) {
            sb.append(move.toString());
            sb.append(moveSeparator);
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

    public ArrayList<Move> getMoves(int id) throws SQLException {
        String query = "SELECT `moves` FROM `games` WHERE `id` = " + id;
        ResultSet rs = db.get(query);
        if (rs.next()) {
            return convertStringToMoves(rs.getString("moves"));
        } else {
            return null;
        }
    }

    public ArrayList<Move> convertStringToMoves(String moves) {
        ArrayList<Move> result = new ArrayList<>();
        String[] movesArray = moves.split(moveSeparator);
        for (String move : movesArray) {
            result.add(moveFromString(move));
        }
        return result;
    }

    public Move moveFromString(String move) {
        String[] moveArray = move.split(" ");
        Color color = Color.valueOf(moveArray[0]);
        Move.Type type = Move.Type.valueOf(moveArray[1]);
        int x = -1;
        int y = -1;
        if (type == Move.Type.MOVE) {
            x = Integer.parseInt(moveArray[2]);
            y = Integer.parseInt(moveArray[3]);
        }
        return new Move(color, type, x, y);
    }

    public String browseGames(int page) throws SQLException {
        String query = "SELECT * FROM `games` ORDER BY `date` DESC LIMIT " + (page - 1) * 10 + ", 10";
        ResultSet rs = db.get(query);
        StringBuilder sb = new StringBuilder();
        while (rs.next()) {
            sb.append(rs.getInt("id"));
            sb.append(" ");
            sb.append(rs.getString("black"));
            sb.append(" ");
            sb.append(rs.getString("white"));
            sb.append(" ");
            sb.append(rs.getString("winner"));
            sb.append(" ");
            sb.append(rs.getInt("boardSize"));
            sb.append(" ");
            sb.append(rs.getString("date"));
            sb.append("\n");
        }
        return sb.toString();
    }
}
