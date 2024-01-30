package org.theGo.database;

import java.sql.*;

/**
 * Singleton class responsible for database connection,
 * executing queries and returning results.
 */
public class Database {
    private final Connection connection;
    private static volatile Database instance;
    private Database() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost/goGame", "root", "");
    }

    /**
     * Returns instance of Database class.
     *
     * @return instance of Database class
     * @throws SQLException if connection to database fails
     */
    public static Database getInstance() throws SQLException {
        if (instance == null) {
            synchronized (Database.class) {
                if (instance == null) {
                    instance = new Database();
                }
            }
        }
        return instance;
    }

    /**
     * Executes a select query and returns result.
     *
     * @param query query to execute
     * @return result of query
     * @throws SQLException if connection to database fails
     */
    public synchronized ResultSet get(String query) throws SQLException {
        if (connection != null && !connection.isClosed()) {
            return connection.createStatement().executeQuery(query);
        } else {
            return null;
        }
    }

    /**
     * Executes an insert or update query.
     *
     * @param query query to execute
     * @throws SQLException if connection to database fails
     */
    public synchronized void insert(String query) throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.createStatement().executeUpdate(query);
        }
    }
}
