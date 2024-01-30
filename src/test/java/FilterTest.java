import org.junit.jupiter.api.Test;
import org.theGo.database.Filter;

public class FilterTest {
    @Test
    public void testBlank() {
        assert new Filter.Clear().getQuery(1).equals("SELECT * FROM `games` WHERE 1=1 ORDER BY `date` DESC LIMIT 0, 10");
    }

    @Test
    public void testNickname() {
        assert new Filter.Nickname("test", new Filter.Clear()).getQuery(1).equals("SELECT * FROM `games` WHERE 1=1 AND `black` = 'test' OR `white` = 'test' ORDER BY `date` DESC LIMIT 0, 10");
    }

    @Test
    public void testCustom() {
        assert new Filter.Custom("test", new Filter.Clear()).getQuery(1).equals("SELECT * FROM `games` WHERE 1=1 AND test ORDER BY `date` DESC LIMIT 0, 10");
    }
}
