import org.apache.commons.text.StringEscapeUtils;
import org.junit.jupiter.api.Test;
import org.theGo.database.Filter;

public class FilterTest {
    @Test
    public void testBlank() {
        assert new Filter.Clear().getQuery(1).strip().equals("SELECT * FROM `games` WHERE 1=1 ORDER BY `date` DESC LIMIT 0, 10");
    }

    @Test
    public void testNickname() {
        System.out.println(StringEscapeUtils.escapeJava(new Filter.Nickname("test", new Filter.Clear()).getQuery(1)));
        assert new Filter.Nickname("test", new Filter.Clear()).getQuery(1).strip().equals("SELECT * FROM `games` WHERE 1=1 AND `black` = 'test' OR `white` = 'test' ORDER BY `date` DESC LIMIT 0, 10");
    }

    @Test
    public void testCustom() {
        System.out.println(StringEscapeUtils.escapeJava(new Filter.Custom("test", new Filter.Clear()).getQuery(1)));
        assert new Filter.Custom("test", new Filter.Clear()).getQuery(1).strip().equals("SELECT * FROM `games` WHERE 1=1 AND test ORDER BY `date` DESC LIMIT 0, 10");
    }
}
