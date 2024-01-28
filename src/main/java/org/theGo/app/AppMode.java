package org.theGo.app;

import org.theGo.communication.Communicator;

/**
 * Abstract class representing application that can be started, and communicates through Communicator
 */
public abstract class AppMode {
    protected Communicator comm;

    /**
     * Abstract class representing application that can be started, and communicates through Communicator
     * @param communicator Communicator to be used by application
     */
    public AppMode(Communicator communicator){
        comm = communicator;
    }
    /**
     * Starts application
     */
    public abstract void start();

}
