import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        ArrayList<String> test = new ArrayList<>();
        // start the system with the default settings
        test.add("3");
        test.add("95"); // current humidity in the steam room = 95
        test.add("42"); // current temperature in the steam room = 42
        test.add("False"); // the schedule for later is not selected
        test.add("1"); // door open
        // temperature = 42.5, humidity = 95.5
        test.add("3"); // cut the water supply
        test.add("5"); // the application is shut down
        Testing testing = new Testing(test);
    }
}
