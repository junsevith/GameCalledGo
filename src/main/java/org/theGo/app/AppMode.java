package org.theGo.app;

import org.theGo.communication.Communicator;

public abstract class AppMode {
    protected Communicator comm;

    public AppMode(Communicator communicator){
        comm = communicator;
    }
    public abstract void start();

}
