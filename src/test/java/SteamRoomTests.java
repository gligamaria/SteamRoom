import junit.framework.TestCase;
import java.util.ArrayList;


public class SteamRoomTests extends TestCase {
    public void testOne() throws InterruptedException {
        ArrayList<String> firstTest = new ArrayList<>();
        firstTest.add("3");
        firstTest.add("42");
        firstTest.add("95");
        firstTest.add("False");
        firstTest.add("4");
        firstTest.add("2");
        firstTest.add("5");
        Testing testing = new Testing(firstTest);
        assertEquals("Current temperature: 95.0\n" +
                "Current humidity: 42.0\n" +
                "Current temperature: 95.0\n" +
                "Current humidity: 43.0\n" +
                "The system has stopped successfully.\n" +
                "-----------------------------\n", testing.output);
    }

}
