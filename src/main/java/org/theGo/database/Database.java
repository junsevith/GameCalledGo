package org.theGo.database;

import java.sql.*;

public class Database {
    final Connection connection;

    private static Database instance;
    private Database() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost/goGame", "root", "");
    }

    public static Database getInstance() throws SQLException {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public ResultSet query(String query) throws SQLException {
        if (connection != null && !connection.isClosed()) {
            return connection.createStatement().executeQuery(query);
        } else {
            return null;
        }
    }
}
