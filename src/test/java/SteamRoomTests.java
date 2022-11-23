import junit.framework.TestCase;
import java.util.ArrayList;


public class SteamRoomTests extends TestCase {

    // C1 Starting the steam room with default settings
    public void testOne() throws InterruptedException {
        ArrayList<String> test = new ArrayList<>();
        test.add("3");     // start the system with the default settings
        test.add("95");    // current humidity in the steam room = 95
        test.add("42");    // current temperature in the steam room = 42
        test.add("False"); // the schedule for later is not selected
        test.add("4");     // no action happens (such as door being opened or water supply cut off)
                           // temperature = 43, humidity = 96
        test.add("4");     // no action happens (such as door being opened or water supply cut off)
                           // temperature = 44, humidity = 97
        test.add("4");     // no action happens (such as door being opened or water supply cut off)
                           // temperature = 44, humidity = 98
        test.add("2");     // the user stops the system, steam room would keep on maintaining the settings
        test.add("5");     // the application is shut down
        Testing testing = new Testing(test);
        assertEquals("Current temperature: 42.0\n" +
                "Current humidity: 95.0\n" +
                "Current temperature: 43.0\n" +
                "Current humidity: 96.0\n" +
                "Current temperature: 44.0\n" +
                "Current humidity: 97.0\n" +
                "The steam room has reached the desired settings.\n" +
                "Current temperature: 44.0\n" +
                "Current humidity: 98.0\n" +
                "The steam room has reached the desired settings.\n" +
                "The system has stopped successfully.\n" +
                "-----------------------------\n", testing.output);
    }

    //C2 Starting the steam room then stopping it
    public void testTwo() throws InterruptedException {
        ArrayList<String> test = new ArrayList<>();
        test.add("3");     // start with default settings
        test.add("95");    // current humidity in the steam room = 95
        test.add("42");    // current temperature in the steam room = 42
        test.add("False"); // the schedule for later is not selected
        test.add("2");     // the user stops the system
        test.add("5");     // the application is shut down
        Testing testing = new Testing(test);
        assertEquals("Current temperature: 42.0\n" +
                "Current humidity: 95.0\n" +
                "The system has stopped successfully.\n" +
                "-----------------------------\n", testing.output);
    }

    //C3 Choosing a Profile when the user has not previously added a profile
    public void testThree() throws InterruptedException {
        ArrayList<String> test = new ArrayList<>();
        test.add("1");   // select profile
        test.add("1");   // choose profile no. 1 (the default one)
        test.add("3");   // start the application
        test.add("95");  // current humidity in the steam room = 95
        test.add("42");  // current temperature in the steam room = 42
        test.add("False"); // the schedule for later is not selected
        test.add("2"); // user stops the system
        test.add("5"); // the application is shut down
        Testing testing = new Testing(test);
        assertEquals("Chosen profile: Default, temperature: 44, humidity: 98\n" +
                "Current temperature: 42.0\n" +
                "Current humidity: 95.0\n" +
                "The system has stopped successfully.\n" +
                "-----------------------------\n", testing.output);
    }

    //C4 Choosing a Profile when the user has created other profiles beforehand
    public void testFour() throws InterruptedException {
        ArrayList<String> test = new ArrayList<>();
        test.add("1"); // select profile
        test.add("2"); // choose profile no. 2 (previously created, not default)
        test.add("3"); // start the application
        test.add("95"); // current humidity in the steam room = 95
        test.add("42"); // current temperature in the steam room = 42
        test.add("False"); // the schedule for later is not selected
        test.add("2"); // user stops the system
        test.add("5"); // the application is shut down
        Testing testing = new Testing(test);
        assertEquals("Chosen profile: Maria, temperature: 46, humidity: 98\n" +
                "Current temperature: 42.0\n" +
                "Current humidity: 95.0\n" +
                "The system has stopped successfully.\n" +
                "-----------------------------\n", testing.output);
    }

    //C5 Creating a Profile with valid data when the user selects yes when confirming their choice
    public void testFive() throws InterruptedException {
        ArrayList<String> test = new ArrayList<>();
        test.add("2"); // create profile
        test.add("test"); // name the profile 'test'
        test.add("45"); // set the temperature to 45
        test.add("98"); // set the humidity for 98
        test.add("1"); // select 'yes' to create the profile
        test.add("5"); // the application is shut down
        Testing testing = new Testing(test);
        assertEquals("Profile created.\n", testing.output);
    }

    //C6 Creating a Profile with valid data when the user selects yes when confirming their
    // choice then the user selects Start and afterwards stops the system
    public void testSix() throws InterruptedException {
        ArrayList<String> test = new ArrayList<>();
        test.add("2"); // create a profile
        test.add("test2"); // name the profile 'test2'
        test.add("45"); // set the temperature to 45
        test.add("98"); // set the humidity for 98
        test.add("1"); // select 'yes' to create the profile
        test.add("1"); // go to select the desired profile
        test.add("4"); // choose profile no. 4
        test.add("3"); // start the system
        test.add("95"); // current humidity is 95
        test.add("42"); // current temperature is 42
        test.add("False"); // no schedule for later
        test.add("2"); // the user stops the steam room
        test.add("5"); // the application is shut down
        Testing testing = new Testing(test);
        assertEquals("Profile created.\n" +
                "Chosen profile: test2, temperature: 45, humidity: 98\n" +
                "Current temperature: 42.0\n" +
                "Current humidity: 95.0\n" +
                "The system has stopped successfully.\n" +
                "-----------------------------\n", testing.output);
    }
    //C7 Creating a Profile with valid data when the user selects no when confirming their choice
    public void testSeven() throws InterruptedException {
        ArrayList<String> firstTest = new ArrayList<>();
        firstTest.add("2"); // create a new profile
        firstTest.add("test3"); // add a valid name
        firstTest.add("45"); // add a valid temperature
        firstTest.add("98"); // add a valid humidity
        firstTest.add("2"); // press no when asked if you want to create a profile => the profile won't be saved
        firstTest.add("5"); // the application is shut down
        Testing testing = new Testing(firstTest);
        assertEquals("", testing.output);
    }
    //C8 Creating a Profile with invalid data for name when the user selects yes when confirming their choice
    public void testEight() throws InterruptedException {
        ArrayList<String> firstTest = new ArrayList<>();
        firstTest.add("2"); // create a new profile
        firstTest.add("Maria"); // name the new profile Maria (which already exists)
        firstTest.add("45"); // add a valid temperature
        firstTest.add("98"); // add a valid humidity
        firstTest.add("test4"); // add a valid new name
        firstTest.add("45"); // add a valid temperature
        firstTest.add("98"); // add a valid humidity
        firstTest.add("1"); // press yes and save the profile
        firstTest.add("5"); // the application is shut down
        Testing testing = new Testing(firstTest);
        assertEquals("The profile should be given a unique name.\n" +
                "Profile created.\n", testing.output);
    }

    //C9 Creating a Profile with invalid data for the temp.
    public void testNine() throws InterruptedException {
        ArrayList<String> firstTest = new ArrayList<>();
        firstTest.add("2"); // create a new profile
        firstTest.add("test5"); // add a valid name
        firstTest.add("40"); // add an invalid temperature
        firstTest.add("98"); // add a valid humidity
        firstTest.add("test5"); // add a valid name
        firstTest.add("45"); // add a valid temperature
        firstTest.add("98"); // add a valid humidity
        firstTest.add("1"); // press yes and save the profile
        firstTest.add("5"); // the application is shut down
        Testing testing = new Testing(firstTest);
        assertEquals("Please insert a temperature >= 43 and <= 46.\n" +
                "Profile created.\n", testing.output);
    }

    //C10 Creating a Profile with invalid data for the humidity
    public void testTen() throws InterruptedException {
        ArrayList<String> firstTest = new ArrayList<>();
        firstTest.add("2"); // create a new profile
        firstTest.add("test6"); // add a valid name
        firstTest.add("45"); // add an invalid temperature
        firstTest.add("70");
        firstTest.add("test6");
        firstTest.add("45");
        firstTest.add("98");
        firstTest.add("1");
        firstTest.add("5"); // the application is shut down
        Testing testing = new Testing(firstTest);
        assertEquals("Please insert a humidity >= 97 and <= 100.\n" +
                "Profile created.\n", testing.output);
    }

    //C11 Scheduling the steam room to work after a set amount of time when the time is too short
    public void testEleven() throws InterruptedException {
        ArrayList<String> test = new ArrayList<>();
        test.add("4"); // start with a schedule for later
        test.add("90"); // current humidity in the steam room = 90
        test.add("42"); // current temperature in the steam room = 42
        test.add("True"); // the schedule for later is selected
        test.add("2"); // scheduled to be ready in 2 minute, but it would take at least 8 minutes to get the room ready
        test.add("5"); // the application is shut down
        Testing testing = new Testing(test);
        assertEquals("Schedule programmed.\n" +
                "There is not enough time for the steam room to be ready.", testing.output);
    }

    //C12 Scheduling the steam room to work after a set amount of time when the time is valid
    public void testTwelve() throws InterruptedException {
        ArrayList<String> test = new ArrayList<>();
        test.add("4"); // start with a schedule for later
        test.add("90"); // current humidity in the steam room = 90
        test.add("42"); // current temperature in the steam room = 42
        test.add("True"); // the schedule for later is selected
        test.add("10"); // scheduled to be ready in 2 minute, but it would take at least 8 minutes to get the room ready
        test.add("4"); // do nothing for 1 iteration
        test.add("2"); // user stops the system
        test.add("5"); // the application is shut down
        Testing testing = new Testing(test);
        assertEquals("Schedule programmed.\n" +
                "Steam room will start in 10 minutes.\n" +
                "Steam room will start in 9 minutes.\n" +
                "Steam room will start in 8 minutes.\n" +
                "Steam room will start in 7 minutes.\n" +
                "Steam room will start in 6 minutes.\n" +
                "Steam room will start in 5 minutes.\n" +
                "Steam room will start in 4 minutes.\n" +
                "Steam room will start in 3 minutes.\n" +
                "Steam room will start in 2 minutes.\n" +
                "Steam room will start in 1 minutes.\n" +
                "Current temperature: 42.0\n" +
                "Current humidity: 90.0\n" +
                "Current temperature: 43.0\n" +
                "Current humidity: 91.0\n" +
                "The system has stopped successfully.\n" +
                "-----------------------------\n", testing.output);
    }

    //C13 Starting the steam room then cutting the water supply
    public void testThirteen() throws InterruptedException {
        ArrayList<String> test = new ArrayList<>();
        test.add("3"); // start with the default settings
        test.add("90"); // current humidity in the steam room = 90
        test.add("42"); // current temperature in the steam room = 42
        test.add("False"); // schedule for later is not selected
        test.add("3"); // cut off the water supply
        test.add("5"); // the application is shut down
        Testing testing = new Testing(test);
        assertEquals("Current temperature: 42.0\n" +
                "Current humidity: 90.0\n" +
                "The system has stopped because there was no water supply.\n" +
                "-----------------------------\n", testing.output);
    }

    //C14 Starting the steam room then opening the door, but closing it in the next iteration
    public void testFourteen() throws InterruptedException {
        ArrayList<String> test = new ArrayList<>();
        test.add("3"); // start with the default settings
        test.add("95"); // current humidity in the steam room = 95
        test.add("42"); // current temperature in the steam room = 42
        test.add("False"); // the schedule for later is not selected
        test.add("1"); // door open
        // temperature = 42.5, humidity = 95.5
        test.add("1"); // close the door
        // temperature = 43.5, humidity = 96.6
        test.add("4"); // no action
        test.add("2"); //stop the steam room
        test.add("5"); // the application is shut down
        Testing testing = new Testing(test);

        assertEquals("Current temperature: 42.0\n" +
                "Current humidity: 95.0\n" +
                "Door is open!\n" +
                "Current temperature: 42.5\n" +
                "Current humidity: 95.5\n" +
                "Current temperature: 43.5\n" +
                "Current humidity: 96.5\n" +
                "Current temperature: 44.5\n" +
                "Current humidity: 97.5\n" +
                "The system has stopped successfully.\n" +
                "-----------------------------\n", testing.output);
    }

    //C15 Leaving the door open for more than 3 minutes
    public void testFifteen() throws InterruptedException {
        ArrayList<String> test = new ArrayList<>();
        test.add("3"); // start the system with the default settings
        test.add("95"); // current humidity in the steam room = 95
        test.add("42"); // current temperature in the steam room = 42
        test.add("False"); // the schedule for later is not selected
        test.add("1"); // door open
        // temperature = 42.5, humidity = 95.5
        test.add("4"); // // no action happens, door is still open
        // temperature = 43, humidity = 96
        test.add("4"); // no action happens, door is still open
        // temperature = 43.5, humidity = 96.5
        test.add("4"); //no action
        test.add("5"); // the application is shut down
        Testing testing = new Testing(test);

        assertEquals("Current temperature: 42.0\n" +
                "Current humidity: 95.0\n" +
                "Door is open!\n" +
                "Current temperature: 42.5\n" +
                "Current humidity: 95.5\n" +
                "Door is open!\n" +
                "Please close the door.\n" +
                "Current temperature: 43.0\n" +
                "Current humidity: 96.0\n" +
                "Door is open!\n" +
                "Please close the door.\n" +
                "Current temperature: 43.5\n" +
                "Current humidity: 96.5\n" +
                "Door is open!\n" +
                "The system stopped as the door has been open for too long.\n" +
                "-----------------------------\n", testing.output);
    }

    //C16 Opening the door then cutting the water supply
    public void testSixteen() throws InterruptedException {

        ArrayList<String> test = new ArrayList<>();
        test.add("3"); // start the system with the default settings
        test.add("95"); // current humidity in the steam room = 95
        test.add("42"); // current temperature in the steam room = 42
        test.add("False"); // the schedule for later is not selected
        test.add("1"); // door open
        // temperature = 42.5, humidity = 95.5
        test.add("3"); // cut the water supply
        test.add("5"); // the application is shut down
        Testing testing = new Testing(test);

        assertEquals("Current temperature: 42.0\n" +
                "Current humidity: 95.0\n" +
                "Door is open!\n" +
                "Current temperature: 42.5\n" +
                "Current humidity: 95.5\n" +
                "Door is open!\n" +
                "Please close the door.\n" +
                "The system has stopped because there was no water supply.\n" +
                "-----------------------------\n", testing.output);
    }
}
