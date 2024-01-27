package org.theGo.players;

import org.theGo.game.Color;
import org.theGo.game.GoBoard;
import org.theGo.game.Move;

public abstract class GoPlayer {
    final Color color;
    String nickname;

    public GoPlayer(Color color) {
        this.color = color;
    }

    public abstract Move takeTurn(GoBoard board);

    public abstract String getName();

    public abstract boolean askFinish();

    public abstract void message(String message);

    public Color getColor() {
        return color;
    }

    public String getNickname() {
    	return nickname;
    }
}
