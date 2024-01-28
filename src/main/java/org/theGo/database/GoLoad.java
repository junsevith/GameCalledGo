package org.theGo.database;

import org.theGo.app.AppMode;
import org.theGo.communication.Communicator;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class GoLoad extends AppMode {
    private final DatabaseHandler dbHandler;

    private int page = 1;

    private String result;

    private Filter filter;

    Map<String, Consumer<String>> commands = new HashMap<>();

    public GoLoad(Communicator comm) throws SQLException {
        super(comm);

        commands.put("h", this::help);
        commands.put("help", this::help);
        commands.put("p", this::prevPage);
        commands.put("prev", this::prevPage);
        commands.put("n", this::nextPage);
        commands.put("next", this::nextPage);
        commands.put("l", this::replayGame);
        commands.put("load", this::replayGame);
        commands.put("r", this::refresh);
        commands.put("refresh", this::refresh);
        commands.put("u", this::filterUser);
        commands.put("user", this::filterUser);
        commands.put("c", this::clearFilter);
        commands.put("clear", this::clearFilter);


        dbHandler = new DatabaseHandler(Database.getInstance());
        clearFilter("");
        refresh("");
    }

    @Override
    public void start() {
        while (true) {
            comm.display(result);
            String command = comm.ask("Podaj komendę: (h - pomoc)");
            if (command.equals("exit")) break;
            String[] args = command.split(" ");
            commands.get(args[0]).accept(args.length > 1 ? args[1] : "");
        }

    }

    private void help(String s) {
        comm.display("""
                h,help - wyświetl pomoc
                p,prev - poprzednia strona
                n,next - następna strona
                l,load - wczytaj powtórkę gry
                r,refresh - odśwież wyniki
                exit - wyjdź z przeglądania gier
                """);
    }

    private void refresh(String s) {
//        comm.message(filter.getQuery(page));
        try {
            result = dbHandler.browseGames(filter.getQuery(page));
        } catch (SQLException e) {
//            throw new RuntimeException(e);
            result = "Błąd podczas wczytywania gier";
        }
    }

    private void nextPage(String s) {
        page++;
        refresh(s);
    }

    private void prevPage(String s) {
        page--;
        refresh(s);
    }

    private void clearFilter(String s) {
        filter = new Filter.Clear();
        refresh(s);
    }

    private void replayGame(String s) {
        int id = Integer.parseInt(s);
        try {
            new GoReplay(comm, dbHandler.getMoves(id)).start();
        } catch (SQLException e) {
            throw new RuntimeException("Error while loading games");
        }
    }

    private void filterUser(String s) {
        filter = new Filter.Nickname(s, filter);
        refresh(s);
    }

}
