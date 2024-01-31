package org.theGo.communication;

import org.theGo.game.Color;
import org.theGo.game.GoBoard;
import org.theGo.game.Move;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * Class that allows to send messages to many communicators at once.
 */
public class Broadcast extends Communicator {

    private final Set<Communicator> communicators;

    public Broadcast(Communicator... communicators) {
        this.communicators = Set.of(communicators);
    }

    @Override
    public String ask(String question, boolean reset) {
        throw new UnsupportedOperationException("Can't ask a question to many communicators");
    }

    @Override
    public boolean confirm(String question, Boolean defaultChoice, boolean reset) {
        throw new UnsupportedOperationException("Can't ask a question to many communicators");
    }

    @Override
    public <T> T choose(String question, Map<String, T> map, List<String> options, Integer defaultChoice, boolean reset) {
        throw new UnsupportedOperationException("Can't ask a question to many communicators");
    }


    @Override
    public <T> T set(String question, Function<String, T> parser, T defaultChoice, boolean reset) {
        throw new UnsupportedOperationException("Can't ask a question to many communicators");
    }

    @Override
    public Move takeMove(String question, Color color) {
        throw new UnsupportedOperationException("Can't ask a question to many communicators");
    }

    @Override
    public void message(String message) {
        communicators.forEach(c -> c.message(message));
    }

    @Override
    public void error(String message) {
        communicators.forEach(c -> c.error(message));
    }

    @Override
    public void display(String message) {
        communicators.forEach(c -> c.display(message));
    }

    @Override
    public void displayBoard(GoBoard board) {
        communicators.forEach(c -> c.displayBoard(board));
    }

    @Override
    public void displayScore(int blackPoints, int whitePoints) {
        communicators.forEach(c -> c.displayScore(blackPoints, whitePoints));
    }

    @Override
    public void close() {
        communicators.forEach(Communicator::close);
    }
}
