import org.junit.jupiter.api.Test;
import org.theGo.game.Color;
import org.theGo.game.Move;

public class MoveTest {
    @Test
    public void testString() {
        assert "PASS".equals(Move.Type.PASS.toString());
        assert "RESIGN".equals(Move.Type.RESIGN.toString());
        assert "MOVE".equals(Move.Type.MOVE.toString());
    }

    @Test
    public void testString2() {
        assert "BLACK MOVE 1 2".equals(new Move(Color.BLACK, Move.Type.MOVE, 1, 2).toString());
    }

    @Test
    public void testIsType(){
        assert new Move(Color.BLACK, Move.Type.MOVE, 1, 2).isType(Move.Type.MOVE);
        assert new Move(Color.BLACK, Move.Type.PASS).isType(Move.Type.PASS);
        assert new Move(Color.BLACK, Move.Type.RESIGN).isType(Move.Type.RESIGN);
    }

    @Test
    public void testXY(){
        assert 1 == new Move(Color.BLACK, Move.Type.MOVE, 1, 2).getX();
        assert 2 == new Move(Color.BLACK, Move.Type.MOVE, 1, 2).getY();
    }

    @Test
    public void testConstructor(){
        assert -1 == new Move(Color.BLACK, Move.Type.PASS).getX();
        assert -1 == new Move(Color.BLACK, Move.Type.PASS).getY();
    }
}
