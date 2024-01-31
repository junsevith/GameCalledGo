package org.theGo.database;

import org.theGo.app.AppMode;
import org.theGo.communication.Communicator;
import org.theGo.game.Move;

import java.sql.SQLException;
import java.util.*;

/**
 * Class responsible for browsing games that are in database
 */
public class GoLoad extends AppMode {
    private final DatabaseHandler dbHandler;
    private int page = 1;

    /**
     * Decorator type filter used for displaying games.
     */
    private Filter filter;

    /**
     * List of commands that can be used in this mode (for displaying purposes).
     */
    private final List<String> commands = Arrays.asList(
            "help",
            "prev",
            "next",
            "load",
            "refresh",
            "user",
            "clear",
            "quit"
    );

    /**
     * Map of commands that can be used in this mode (for executing purposes).
     */
    private final Map<String, Runnable> commandSet = new HashMap<>();

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
        commandSet.put("q", this::exit);
        commandSet.put("quit", this::exit);


        dbHandler = new DatabaseHandler(Database.getInstance());
        clearFilter();
    }

    /**
     * Flag that indicates if this mode should continue running.
     */
    private boolean cont = true;

    @Override
    public void start() {
        while (cont) {
            comm.choose("Podaj komendę: (h - pomoc)", commandSet, commands, 0, false).run();
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
                q,quit - wyjdź z przeglądania gier
                """);
    }

    private void exit() {
        cont = false;
    }

    private void refresh() {
//        comm.message(filter.getQuery(page));
        String result;
        try {
            String query = filter.getQuery(page);
//            System.out.println(query);
            result = dbHandler.browseGames(query);
        } catch (SQLException e) {
//            throw new RuntimeException(e);
            result = "Błąd podczas wczytywania gier";
        }
        result = result.replace('_', ' ').replace('#', 'H');
        comm.display(result);
    }

    private void nextPage() {
        page++;
        refresh();
    }

    private void prevPage() {
        if (page > 1) {
            page--;
        }
        refresh();
    }

    private void clearFilter() {
        filter = new Filter.Clear();
        refresh();
    }

    private void replayGame() {
        int id = comm.set("Podaj id gry: ", Integer::parseInt, null, false);
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
        String s = comm.ask("Podaj nazwę użytkownika: ", false);
        filter = new Filter.Nickname(s, filter);
        refresh();
    }

}
