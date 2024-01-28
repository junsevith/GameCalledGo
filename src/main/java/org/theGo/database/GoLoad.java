package org.theGo.database;

import org.theGo.app.AppMode;
import org.theGo.communication.Communicator;
import org.theGo.game.Move;

import java.sql.SQLException;
import java.util.*;

public class GoLoad extends AppMode {
    private final DatabaseHandler dbHandler;
    private int page = 1;

    private Filter filter;

    private final List<String> commands = Arrays.asList(
            "help",
            "prev",
            "next",
            "load",
            "refresh",
            "user",
            "clear",
            "exit"
    );

    Map<String, Runnable> commandSet = new HashMap<>();

    public GoLoad(Communicator comm) throws SQLException {
        super(comm);

        commandSet.put("h", this::help);
        commandSet.put("help", this::help);
        commandSet.put("p", this::prevPage);
        commandSet.put("prev", this::prevPage);
        commandSet.put("n", this::nextPage);
        commandSet.put("next", this::nextPage);
        commandSet.put("l", this::replayGame);
        commandSet.put("load", this::replayGame);
        commandSet.put("r", this::refresh);
        commandSet.put("refresh", this::refresh);
        commandSet.put("u", this::filterUser);
        commandSet.put("user", this::filterUser);
        commandSet.put("c", this::clearFilter);
        commandSet.put("clear", this::clearFilter);
        commandSet.put("e", this::exit);
        commandSet.put("exit", this::exit);


        dbHandler = new DatabaseHandler(Database.getInstance());
        clearFilter();
    }

    private boolean cont = true;

    @Override
    public void start() {
        while (cont) {
            comm.choose("Podaj komendę: (h - pomoc)", commandSet, commands, 0).run();
        }
    }

    private void help() {
        comm.display("""
                h,help - wyświetl pomoc
                p,prev - poprzednia strona
                n,next - następna strona
                l,load - wczytaj powtórkę gry
                r,refresh - odśwież wyniki
                u,user - filtruj po użytkowniku
                c,clear - wyczyść filtr
                e,exit - wyjdź z przeglądania gier
                """);
    }

    private void exit() {
        cont = false;
    }

    private void refresh() {
//        comm.message(filter.getQuery(page));
        String result;
        try {
            result = dbHandler.browseGames(filter.getQuery(page));
        } catch (SQLException e) {
//            throw new RuntimeException(e);
            result = "Błąd podczas wczytywania gier";
        }
        comm.display(result);
    }

    private void nextPage() {
        page++;
        refresh();
    }

    private void prevPage() {
        page--;
        refresh();
    }

    private void clearFilter() {
        filter = new Filter.Clear();
        refresh();
    }

    private void replayGame() {
        int id  = comm.set("Podaj id gry: ", Integer::parseInt, null);
        try {
            ArrayList<Move> moves = dbHandler.getMoves(id);
            int size = moves.getFirst().getX();
            moves.removeFirst();
            new GoReplay(comm, moves, size);
        } catch (SQLException e) {
            comm.error("Błąd podczas wczytywania gry");
        }
    }

    private void filterUser() {
        String s = comm.ask("Podaj nazwę użytkownika: ");
        filter = new Filter.Nickname(s, filter);
        refresh();
    }

}
