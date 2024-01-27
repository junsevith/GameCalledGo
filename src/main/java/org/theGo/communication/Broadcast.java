package org.theGo.communication;

import org.theGo.game.GoBoard;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class Broadcast extends Communicator{

    Set<Communicator> communicators;

    public Broadcast(Communicator... communicators){
        this.communicators = Set.of(communicators);
    }

    @Override
    public String ask(String question) {
        throw new UnsupportedOperationException("Can't ask a question to many communicators");
    }

    @Override
    public boolean confirm(String question, Boolean defaultChoice) {
        throw new UnsupportedOperationException("Can't ask a question to many communicators");
    }

    @Override
    public <T> T choose(String question, Map<String, T> map, List<String> options, Integer defaultChoice) {
        throw new UnsupportedOperationException("Can't ask a question to many communicators");
    }



    @Override
    public <T> T set(String question, Function<String, T> parser, T defaultChoice) {
        throw new UnsupportedOperationException("Can't ask a question to many communicators");
    }

    @Override
    public void message(String message) {
        communicators.forEach(c -> c.message(message));
    }

    @Override
    public void accept(String message) {

    }

    @Override
    public void deny(String message) {

    }

    @Override
    public void display(String message) {
        communicators.forEach(c -> c.display(message)) ;
    }

    @Override
    public void displayBoard(GoBoard board) {
        communicators.forEach(c -> c.displayBoard(board));
    }

    @Override
    public void displayScore(int blackPoints, int whitePoints) {
        communicators.forEach(c -> c.displayScore(blackPoints, whitePoints));
    }
}
