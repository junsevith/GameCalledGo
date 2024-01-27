package org.theGo.players;

import org.theGo.game.Color;
import org.theGo.game.GoBoard;
import org.theGo.game.Move;
import org.theGo.communication.Communicator;

public class HumanPlayer extends GoPlayer {
    Communicator comm;

    public HumanPlayer(Color color, Communicator communicator) {
        super(color);
        comm = communicator;
    }

    @Override
    public Move takeTurn(GoBoard board) {
        Move output = null;
        while (output == null) {
            try {
                String line = comm.ask("Podaj współrzędne ruchu:");
                String[] args = line.split(" ");

                if (line.strip().equals("pas")) {
                    output = new Move(super.color, Move.Type.PASS);
                    continue;
                }

                if (line.strip().equals("resign")) {
                    output = new Move(super.color, Move.Type.RESIGN);
                    continue;
                }

                int x = Integer.parseInt(args[0]);
                int y = Integer.parseInt(args[1]);

                if (x < 1 || x > board.getSize() || y < 1 || y > board.getSize()) {
                    throw new IllegalArgumentException();
                }

                if (!board.placeStone(x, y, super.color)) {
                    throw new IllegalArgumentException();
                } else {
                    output = new Move(super.color, Move.Type.MOVE, x, y);
                }

            } catch (NumberFormatException e) {
                comm.deny("Niepoprawne współrzędne");
            } catch (IllegalArgumentException e) {
                comm.deny("Nie można ustawić kamienia na tym polu");
            } catch (Exception e) {
                comm.deny("Niepoprawny format danych");
            }
        }
        return output;
    }

    @Override
    public String getName() {
        String output = "";
        if (nickname != null) {
            output += nickname + " ";
        }

        if (super.color == Color.BLACK) {
            output += "grający czarnymi";
        } else {
            output += "grający białymi";
        }
        return output;
    }

    @Override
    public boolean askFinish() {
        return comm.confirm("Czy chcesz zakończyć grę?", false);
    }

    @Override
    public void message(String message) {
        comm.message(message);
    }

    @Override
    public String getNickname() {
    	if (nickname == null) {
            nickname = comm.ask(getName() + ", wpisz swój nick (pozwoli on ci na znalezienie zapisu gry w bazie danych):");
        }
        return nickname;
    }
}


