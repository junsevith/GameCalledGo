package org.theGo.players;

import org.theGo.Color;
import org.theGo.GoBoard;

public abstract class GoPlayer {
    final Color color;

    public GoPlayer(Color color) {
        this.color = color;
    }

    public abstract String takeTurn(GoBoard board);

    public abstract String getName();

    public abstract boolean askFinish();

    public abstract void message(String message);

    public Color getColor() {
        return color;
    }

}
