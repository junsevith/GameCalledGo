package org.theGo.communication;

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
    public <T> T choose(String question, Map<String, T> map, T defaultChoice) {
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
}
