import org.junit.jupiter.api.Test;
import org.theGo.app.GoSetup;
import org.theGo.communication.TermComm;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class GoGameTest {
    @Test
    public void killTest(){
        String ring = """



            1 2
            2 2
            1 3
            2 3
            1 4
            2 4
            2 5
            3 4
            3 5
            4 3
            4 5
            4 2
            5 4
            3 2
            5 3
            4 4
            5 2
            9 8
            4 1
            9 7
            3 1
            9 6
            2 1
            9 5
            3 3
            pas
            pas
            t
            t
            """;
        InputStream in = new ByteArrayInputStream(ring.getBytes());
        new GoSetup(new TermComm(in, System.out));
    }

    @Test
    public void koTest() {
        String ko = """
                            
                     
                            
                1 2
                2 2
                2 1
                3 1
                2 3
                3 3
                3 2
                4 2
                9 9
                2 2
                3 2
                9 8
                9 7
                3 2
                pas
                pas
                t
                t
                """;
        InputStream in = new ByteArrayInputStream(ko.getBytes());
        new GoSetup(new TermComm(in, System.out));
    }
}
