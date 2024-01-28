package org.theGo.database;

import org.theGo.game.Color;
import org.theGo.game.Move;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Class responsible for constructing queries and executing them.
 * It also handles converting data from database to objects and vice versa.
 */
public class DatabaseHandler {
    public String moveSeparator = "#";
    private final Database db;

    public DatabaseHandler(Database db) {
        this.db = db;
    }

    /**
     * Saves game to database.
     *
     * @param blackNick black player's nickname
     * @param whiteNick white player's nickname
     * @param winner    winner's nickname
     * @param boardSize size of the board
     * @param moves     list of moves that were made during the game
     */
    public void saveGame(String blackNick, String whiteNick, String winner, int boardSize, ArrayList<Move> moves) throws SQLException {
        String query = "INSERT INTO `games` (`id`, `black`, `white`, `winner`, `boardSize`, `moves`,`date`) VALUES (NULL, '" + blackNick + "', '" + whiteNick + "', '" + winner + "', '" + boardSize + "', '" + convertMovesToString(moves) + "', NOW());";
        db.insert(query);
    }

    /**
     * Converts list of moves to string.
     *
     * @param moves list of moves
     * @return string representation of moves
     */
    public String convertMovesToString(ArrayList<Move> moves) {
        StringBuilder sb = new StringBuilder();
        for (Move move : moves) {
            sb.append(move.toString());
            sb.append(moveSeparator);
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /**
     * Returns list of moves from database entry with given id.
     *
     * @param id id of the game
     * @return list of moves
     */
    public ArrayList<Move> getMoves(int id) throws SQLException {
        String query = "SELECT `moves`,`boardSize` FROM `games` WHERE `id` = " + id;
        ResultSet rs = db.get(query);
        ArrayList<Move> result = new ArrayList<>();
        if (rs.next()) {
            result.add(new Move(null, null, rs.getInt("boardSize"), 0));
            result.addAll(convertStringToMoves(rs.getString("moves")));
        }
        return result;
    }

    /**
     * Returns list of moves from database entry with given id.
     *
     * @param moves string representation of moves
     * @return list of moves
     */
    public ArrayList<Move> convertStringToMoves(String moves) {
        ArrayList<Move> result = new ArrayList<>();
        String[] movesArray = moves.split(moveSeparator);
        for (String move : movesArray) {
            result.add(moveFromString(move));
        }
        return result;
    }

    /**
     * Converts string to Move object.
     *
     * @param move string representation of move
     * @return Move object
     */
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

    /**
     * Returns list of games from database.
     *
     * @param query query to execute
     * @return list of games
     */
    public String browseGames(String query) throws SQLException {
        return displayGames(db.get(query));
    }


    /**
     * Converts ResultSet to string for displaying purposes.
     *
     * @param rs ResultSet to convert
     * @return string representation of ResultSet
     * @throws SQLException if connection to database fails
     */
    private String displayGames(ResultSet rs) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("id black white winner boardSize date\n");
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
