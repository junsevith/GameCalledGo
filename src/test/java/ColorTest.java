import org.junit.jupiter.api.Test;
import org.theGo.game.Color;
import org.theGo.game.Move;

public class ColorTest {
    @Test
    public void testString() {
        assert "BLACK".equals(Color.BLACK.toString());
        assert "WHITE".equals(Color.WHITE.toString());
    }

    @Test
    public void testOpposite() {
        assert Color.WHITE == Color.BLACK.opposite();
        assert Color.BLACK == Color.WHITE.opposite();
    }

    @Test
    public void testGetColor() {
        assert Color.BLACK == new Move(Color.BLACK, Move.Type.MOVE, 1, 2).getColor();
        assert Color.WHITE == new Move(Color.WHITE, Move.Type.MOVE, 1, 2).getColor();
    }
}
