package org.theGo.database;

import org.theGo.app.AppMode;
import org.theGo.communication.Communicator;

import java.sql.SQLException;

public class GoLoad extends AppMode {
    DatabaseHandler dbHandler;
    public GoLoad(Communicator comm) throws SQLException {
        super(comm);
        dbHandler = new DatabaseHandler(Database.getInstance());
    }

    @Override
    public void start() {
        int page = 1;
        boolean exit = false;
        while (!exit){

        }
        try {
            comm.display(dbHandler.browseGames(1));
        } catch (SQLException e) {
            throw new RuntimeException("Error while loading games");
        }
    }

    private void displayGames() {

    }

}
