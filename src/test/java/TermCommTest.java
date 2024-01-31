import org.junit.jupiter.api.Test;
import org.theGo.communication.Communicator;
import org.theGo.communication.TermComm;
import org.theGo.game.GoBoard;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

public class TermCommTest {
    @Test
    public void messageTest() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        Communicator comm = new TermComm(System.in, outContent, System.err);
        comm.message("test");
//        System.out.println(StringEscapeUtils.escapeJava(outContent.toString()));
        assert outContent.toString().strip().equals("test");
    }

    @Test
    public void askTest() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        ByteArrayInputStream inContent = new ByteArrayInputStream("answer".getBytes());
        Communicator comm = new TermComm(inContent, outContent, System.err);
        assert comm.ask("question", ).equals("answer");
        assert outContent.toString().strip().equals("question");
    }

    @Test
    public void askYesNoTest() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        ByteArrayInputStream inContent = new ByteArrayInputStream("y".getBytes());
        Communicator comm = new TermComm(inContent, outContent, System.err);
        assert comm.confirm("question",null, );
        assert outContent.toString().strip().equals("question (y/n)");
    }

    @Test
    public void setTest() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        ByteArrayInputStream inContent = new ByteArrayInputStream("12".getBytes());
        Communicator comm = new TermComm(inContent, outContent, System.err);
        assert comm.set("question",Integer::parseInt,null, ).equals(12);
        assert outContent.toString().strip().equals("question");
    }

    @Test
    public void chooseTest() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        ByteArrayInputStream inContent = new ByteArrayInputStream("option1".getBytes());
        Communicator comm = new TermComm(inContent, outContent, System.err);
        Integer option = comm.choose("question", Map.of(
                "option1", 1,
                "option2", 2
        ), List.of("option1","option2"),null, );
        assert option == 1;
        assert outContent.toString().strip().equals("question (type o to see options)");
    }

    @Test
    public void displayTest() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        Communicator comm = new TermComm(System.in, outContent, System.err);
        comm.display("test");
        assert outContent.toString().strip().equals("test");
    }

    @Test
    public void errorTest() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        Communicator comm = new TermComm(System.in, System.out, outContent);
        comm.error("test");
        assert outContent.toString().strip().equals("test");
    }

    @Test
    public void printBoardTest() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        Communicator comm = new TermComm(System.in, outContent, System.err);
        comm.displayBoard(new GoBoard(5));
        assert outContent.toString().equals("    1  2  3  4  5\n 1  +  +  +  +  +  \n 2  +  +  +  +  +  \n 3  +  +  +  +  +  \n 4  +  +  +  +  +  \n 5  +  +  +  +  +  \r\n");
    }

    @Test
    public void displayScoreTest() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        Communicator comm = new TermComm(System.in, outContent, System.err);
        comm.displayScore(13,9);
//        System.out.println(StringEscapeUtils.escapeJava(outContent.toString()));
        assert outContent.toString().equals("""
                Punkty czarnego: 13
                Punkty białego: 9\r
                """);
    }

    @Test
    public void displayTextTest() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        Communicator comm = new TermComm(System.in, outContent, System.err);
        comm.display("test");
        assert outContent.toString().strip().equals("test");
    }
}
